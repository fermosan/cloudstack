/**
 *  Copyright (C) 2010 Cloud.com, Inc.  All rights reserved.
 * 
 * This software is licensed under the GNU General Public License v3 or later.
 * 
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.cloud.api.commands;
import org.apache.log4j.Logger;

import com.cloud.api.ApiConstants;
import com.cloud.api.ApiResponseHelper;
import com.cloud.api.BaseCmd;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.response.DiskOfferingResponse;
import com.cloud.configuration.ConfigurationManager;
import com.cloud.storage.DiskOfferingVO;

@Implementation(method="updateDiskOffering", manager=ConfigurationManager.class, description="Updates a disk offering.")
public class UpdateDiskOfferingCmd extends BaseCmd{
    public static final Logger s_logger = Logger.getLogger(UpdateDiskOfferingCmd.class.getName());
    private static final String s_name = "updatediskofferingresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.DISPLAY_TEXT, type=CommandType.STRING, description="updates alternate display text of the disk offering with this value")
    private String displayText;

    @Parameter(name=ApiConstants.ID, type=CommandType.LONG, required=true, description="ID of the disk offering")
    private Long id;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, description="updates name of the disk offering with this value")
    private String diskOfferingName;

    @Parameter(name=ApiConstants.TAGS, type=CommandType.STRING, description="update tags of the disk offering with this value")
    private String tags;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getDisplayText() {
        return displayText;
    }

    public Long getId() {
        return id;
    }

    public String getDiskOfferingName() {
        return diskOfferingName;
    }

    public String getTags() {
        return tags;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getName() {
        return s_name;
    }
    
    @Override @SuppressWarnings("unchecked")
    public DiskOfferingResponse getResponse() {
        DiskOfferingVO offering = (DiskOfferingVO) getResponseObject();
        DiskOfferingResponse response = ApiResponseHelper.createDiskOfferingResponse(offering);
        response.setResponseName(getName());
        return response;
    }
}
