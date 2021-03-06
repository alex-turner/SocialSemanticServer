/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
* For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
 package at.kc.tugraz.ss.service.rating.datatypes;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;

public class SSRating{

  public SSUri  uri;
  public int    score;
  public SSUri  resource;
  public SSUri  user;
  public long   timestamp;

  public static SSRating get(
    SSUri  uri,
    int    score,
    SSUri  resource,
    SSUri  user,
    long   timestamp){
    
    return new SSRating(uri, score, resource, user, timestamp);
  }
  
  private SSRating(
    SSUri  uri,
    int    score,
    SSUri  resource,
    SSUri  user,
    long   timestamp){
    
    this.uri           = uri;
    this.score         = score;
    this.resource      = resource;
    this.user          = user;
    this.timestamp     = timestamp;
  }
}