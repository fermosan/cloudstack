// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.quota;

import com.cloud.user.AccountVO;
import com.cloud.user.dao.AccountDao;
import com.cloud.utils.db.TransactionLegacy;
import junit.framework.TestCase;
import org.apache.cloudstack.framework.config.dao.ConfigurationDao;
import org.apache.cloudstack.quota.QuotaStatementImpl.STATEMENT_PERIODS;
import org.apache.cloudstack.quota.dao.QuotaAccountDao;
import org.apache.cloudstack.quota.dao.QuotaUsageDao;
import org.apache.cloudstack.quota.vo.QuotaAccountVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.naming.ConfigurationException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class QuotaStatementTest extends TestCase {

    @Mock
    AccountDao accountDao;
    @Mock
    QuotaAccountDao quotaAcc;
    @Mock
    ConfigurationDao configDao;
    @Mock
    QuotaUsageDao quotaUsage;
    @Mock
    QuotaAlertManager emailQuotaAlert;

    @Spy
    QuotaStatementImpl quotaStatement = new QuotaStatementImpl();

    private void injectMockToField(Object mock, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = QuotaStatementImpl.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(quotaStatement, mock);
    }

    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException, ConfigurationException {
        // Dummy transaction stack setup
        TransactionLegacy.open("QuotaStatementImplTest");

        injectMockToField(accountDao, "_accountDao");
        injectMockToField(quotaAcc, "_quotaAcc");
        injectMockToField(configDao, "_configDao");
        injectMockToField(quotaUsage, "_quotaUsage");
        injectMockToField(emailQuotaAlert, "_quotaAlert");
    }

    @Test
    public void testStartStop() {
        try {
            quotaStatement.start(); // expected to fail as pid is not available
        } catch (NumberFormatException ignored) {
        }
        assertTrue(quotaStatement.stop());
    }

    @Test
    public void testStatementPeriod() {
        Calendar date = Calendar.getInstance();

        //BIMONTHLY - first statement of month
        date.set(Calendar.DATE, QuotaStatementImpl.s_LAST_STATEMENT_SENT_DAYS + 1);
        Calendar period[] = quotaStatement.statementTime(date, STATEMENT_PERIODS.BIMONTHLY);
        assertTrue(period == null);

        //1 of this month
        date.set(Calendar.DATE, 1);
        period = quotaStatement.statementTime(date, STATEMENT_PERIODS.BIMONTHLY);
        assertTrue(period != null);
        assertTrue(period[0].toString(), period[0].before(period[1]));
        assertTrue(period[0].toString(), period[0].get(Calendar.DATE) == 1);
        assertTrue(period[1].toString(), period[1].get(Calendar.DATE) == 15);

        //BIMONTHLY - second statement of month
        date.set(Calendar.DATE, QuotaStatementImpl.s_LAST_STATEMENT_SENT_DAYS + 16);
        period = quotaStatement.statementTime(date, STATEMENT_PERIODS.BIMONTHLY);
        assertTrue(period == null);

        //17 of this month
        date.set(Calendar.DATE, 17);
        period = quotaStatement.statementTime(date, STATEMENT_PERIODS.BIMONTHLY);
        assertTrue(period != null);
        assertTrue(period[0].toString(), period[0].before(period[1]));
        assertTrue(period[0].toString(), period[0].get(Calendar.DATE) == 16);

        //get last day of the previous month
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.add(Calendar.MONTH, -1);
        aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1);

        assertTrue(period[1].toString(), period[1].get(Calendar.DATE) == aCalendar.get(Calendar.DATE));

        //MONTHLY
        date.set(Calendar.DATE, QuotaStatementImpl.s_LAST_STATEMENT_SENT_DAYS + 1);
        period = quotaStatement.statementTime(date, STATEMENT_PERIODS.MONTHLY);
        assertTrue(period == null);

        //1 of this month
        date.set(Calendar.DATE, QuotaStatementImpl.s_LAST_STATEMENT_SENT_DAYS - 1);
        period = quotaStatement.statementTime(date, STATEMENT_PERIODS.MONTHLY);
        assertTrue(period != null);
        assertTrue(period[0].toString(), period[0].before(period[1]));
        assertTrue(period[0].toString(), period[0].get(Calendar.DATE) == 1);

        //get last day of the previous month
        aCalendar = Calendar.getInstance();
        aCalendar.add(Calendar.MONTH, -1);
        aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1);

        assertTrue(period[1].toString(), period[1].get(Calendar.DATE) == aCalendar.get(Calendar.DATE));

    }

    @Test
    public void testSendStatement() {
        AccountVO accountVO = new AccountVO();
        accountVO.setId(2L);
        accountVO.setDomainId(1L);
        Mockito.when(accountDao.findById(Mockito.anyLong())).thenReturn(accountVO);

        QuotaAccountVO acc = new QuotaAccountVO(2L);
        acc.setQuotaBalance(new BigDecimal(404));
        acc.setLastStatementDate(null);
        List<QuotaAccountVO> accounts = new ArrayList<>();
        accounts.add(acc);
        Mockito.when(quotaAcc.listAllQuotaAccount()).thenReturn(accounts);

        Mockito.when(quotaUsage.findTotalQuotaUsage(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyInt(), Mockito.any(Date.class), Mockito.any(Date.class)))
                .thenReturn(new BigDecimal(100));

        // call real method on send monthly statement
        Mockito.doCallRealMethod().when(quotaStatement).sendStatement();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DATE, 1); // simulate sending statement on 1st of the month
        quotaStatement.sendStatement();
        Mockito.verify(emailQuotaAlert, Mockito.times(accounts.size())).sendQuotaAlert(Mockito.any(QuotaAlertManagerImpl.DeferredQuotaEmail.class));
    }

}