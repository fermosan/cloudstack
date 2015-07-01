//Licensed to the Apache Software Foundation (ASF) under one
//or more contributor license agreements.  See the NOTICE file
//distributed with this work for additional information
//regarding copyright ownership.  The ASF licenses this file
//to you under the Apache License, Version 2.0 (the
//"License"); you may not use this file except in compliance
//with the License.  You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing,
//software distributed under the License is distributed on an
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//KIND, either express or implied.  See the License for the
//specific language governing permissions and limitations
//under the License.
package org.apache.cloudstack.api.response;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseResponse;

import com.cloud.serializer.Param;

public class QuotaCreditsResponse extends BaseResponse {

    @SerializedName(ApiConstants.ID)
    @Param(description = "the ID of the credit")
    private String id;

    @SerializedName(ApiConstants.ACCOUNT)
    @Param(description = "the account name of the api remaining count")
    private String accountName;

    @SerializedName(ApiConstants.DOMAIN_ID)
    @Param(description = "the domain ID of the iam policy")
    private String domainId;

    @SerializedName("credits")
    @Param(description = "the credit deposited")
    private String credits;

    @SerializedName("balance")
    @Param(description = "the balance credit in account")
    private String balance;

    @SerializedName("updated_by")
    @Param(description = "the account name of the admin who updated the credits")
    private String updatedBy;


    @SerializedName("updated_on")
    @Param(description = "the account name of the admin who updated the credits")
    private Timestamp updatedOn;


     public QuotaCreditsResponse() {
         super();
     }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getAccountName() {
        return accountName;
    }


    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public String getDomainId() {
        return domainId;
    }


    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public String getCredits() {
        return credits;
    }


    public void setCredits(String credits) {
        this.credits = credits;
    }


    public String getBalance() {
        return balance;
    }


    public void setBalance(String balance) {
        this.balance = balance;
    }


    public String getUpdatedBy() {
        return updatedBy;
    }


    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }


    public Timestamp getUpdatedOn() {
        return updatedOn;
    }


    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }



}