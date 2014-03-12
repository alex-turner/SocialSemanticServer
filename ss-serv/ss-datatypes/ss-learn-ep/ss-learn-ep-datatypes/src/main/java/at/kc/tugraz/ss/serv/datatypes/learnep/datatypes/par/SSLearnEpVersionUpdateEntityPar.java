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
package at.kc.tugraz.ss.serv.datatypes.learnep.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSLearnEpVersionUpdateEntityPar extends SSServPar{
  
  public SSUri    learnEpEntityUri   = null;
  public SSUri    entityUri          = null;
  public Float    x                  = null;
  public Float    y                  = null;
  
  public SSLearnEpVersionUpdateEntityPar(SSServPar par) throws Exception{
      
    super(par);
    
    try{
      
      if(pars != null){
        learnEpEntityUri   = (SSUri)      pars.get(SSVarU.learnEpEntityUri); 
        entityUri          = (SSUri)      pars.get(SSVarU.entityUri);
        x                  = (Float)    pars.get(SSVarU.x);
        y                  = (Float)    pars.get(SSVarU.y);
      }
      
      if(clientPars != null){
        learnEpEntityUri  = SSUri.get        (clientPars.get(SSVarU.learnEpEntityUri));
        entityUri         = SSUri.get        (clientPars.get(SSVarU.entityUri));
        x                 = Float.parseFloat (clientPars.get(SSVarU.x));
        y                 = Float.parseFloat (clientPars.get(SSVarU.y));
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}