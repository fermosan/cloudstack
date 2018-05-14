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

package org.apache.cloudstack.backup;

import java.util.List;

import org.apache.cloudstack.api.response.BackupPolicyResponse;
import org.apache.cloudstack.framework.backup.BackupPolicy;
import org.apache.cloudstack.framework.backup.BackupService;
import org.apache.cloudstack.framework.config.ConfigKey;
import org.apache.cloudstack.framework.config.Configurable;

import com.cloud.utils.component.Manager;
import com.cloud.utils.component.PluggableService;

/**
 * Backup and Recover Manager Interface
 */
public interface BackupManager extends BackupService, Configurable, PluggableService, Manager {

    ConfigKey<Boolean> BackupFrameworkEnabled = new ConfigKey<>("Advanced", Boolean.class,
            "backup.framework.enabled",
            "false",
            "Is backup and recovery framework enabled.", true, ConfigKey.Scope.Zone);

    ConfigKey<String> BackupProviderPlugin = new ConfigKey<>("Advanced", String.class,
            "backup.framework.provider.plugin",
            "",
            "The backup and recovery provider plugin.", true, ConfigKey.Scope.Zone);

    /**
     * Generate a response from the Backup Policy VO
     */
    BackupPolicyResponse createBackupPolicyResponse(BackupPolicy policyVO);

    /**
     * Add a new Backup and Recovery policy
     */
    BackupPolicy addBackupPolicy(String policyId, String policyName, Long zoneId);

    /**
     * Assign VM to existing backup policy
     */
    boolean assignVMToBackupPolicy(String policyUuid, Long virtualMachineId, Long zoneId);

    /**
     * List existing backup policies
     */
    List<BackupPolicy> listBackupPolicies();

    /**
     * List backup policies existing on the backup provider
     */
    List<BackupPolicy> listBackupProviderPolicies(Long zoneId);

    /**
     * Deletes a backup policy
     */
    boolean deleteBackupPolicy(String policyId);
}