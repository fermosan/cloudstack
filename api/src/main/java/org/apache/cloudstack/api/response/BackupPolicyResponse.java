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
package org.apache.cloudstack.api.response;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseResponse;
import org.apache.cloudstack.api.EntityReference;
import org.apache.cloudstack.framework.backup.BackupPolicy;

import com.cloud.serializer.Param;
import com.google.gson.annotations.SerializedName;

@EntityReference(value = BackupPolicy.class)
public class BackupPolicyResponse extends BaseResponse {

    @SerializedName(ApiConstants.ID)
    @Param(description = "internal id of the backup policy")
    private String id;

    @SerializedName(ApiConstants.NAME)
    @Param(description = "internal name for the backup policy")
    private String name;

    @SerializedName(ApiConstants.POLICY_ID)
    @Param(description = "policy id on the provider side")
    private String policyId;

    public void setId(String id) {
        this.id = id;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public void setName(String name) {
        this.name = name;
    }
}