/*******************************************************************************
 * Copyright 2016 General Electric Company.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.ge.digital.simplepredixservice.ingester;

import com.ge.predix.timeseries.model.datapoints.DataPoint;

/**
 * Object to hold a datapoint and its tag
 */
class TaggedDataPoint {
    private String tag;
    private DataPoint dataPoint;

    TaggedDataPoint(String tag, DataPoint dataPoint) {
        this.tag = tag;
        this.dataPoint = dataPoint;
    }

    DataPoint getDataPoint() {
        return dataPoint;
    }

    String getTag() {
        return tag;
    }

}
