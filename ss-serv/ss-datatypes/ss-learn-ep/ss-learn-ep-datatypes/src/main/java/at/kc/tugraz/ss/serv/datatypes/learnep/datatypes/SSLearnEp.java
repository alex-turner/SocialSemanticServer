/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityA;
import java.util.HashMap;
import java.util.Map;

public class SSLearnEp extends SSEntityA {

  public SSUri       learnEpUri = null;
  public SSUri       user       = null;
  public SSLabelStr  label      = null;

  public static SSLearnEp get(SSUri user, SSUri learnEpUri, SSLabelStr label){
    return new SSLearnEp(user, learnEpUri, label);
  }
  
  private SSLearnEp(SSUri user, SSUri learnEpUri, SSLabelStr label){
    
    super(learnEpUri);
    
    this.user        = user;
    this.learnEpUri  = learnEpUri;
    this.label       = label;
  }
  
  @Override
  public Object jsonLDDesc(){
    
    Map<String, Object> ld         = new HashMap<String, Object>();
    
    ld.put(SSVarU.user,       SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.learnEpUri, SSVarU.sss + SSStrU.colon + SSUri.class.getName());
    ld.put(SSVarU.label,      SSVarU.sss + SSStrU.colon + SSSpaceEnum.class.getName());
    
    return ld;
  }
  
  /*************** getters to allow for json enconding ********************/
  public String getUser() throws Exception {
    return SSUri.toStrWithoutSlash(user);
  }

  public String getLearnEpUri() throws Exception {
    return SSUri.toStrWithoutSlash(learnEpUri);
  }

  public String getLabel() {
    return SSLabelStr.toStr(label);
  }
}