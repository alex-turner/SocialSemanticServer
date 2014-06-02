/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par;

import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import java.util.ArrayList;
import java.util.List;

public class SSEntityUserCircleCreatePar extends SSServPar{

  public List<SSUri>            entities    = new ArrayList<SSUri>();
  public List<SSUri>            users       = new ArrayList<SSUri>();
  public SSCircleE              type        = null;
  public SSLabel                label       = null;
  
  public SSEntityUserCircleCreatePar(final SSServPar par) throws Exception{
    
    super(par);
    
    try{
    
      if(pars != null){
        type       = (SSCircleE)       pars.get(SSVarU.type);
        label      = (SSLabel)         pars.get(SSVarU.label);
        entities   = (List<SSUri>)     pars.get(SSVarU.entities);
        users      = (List<SSUri>)     pars.get(SSVarU.users);
      }
      
      if(clientPars != null){
        type       = SSCircleE.get       (clientPars.get(SSVarU.type));
        label      = SSLabel.get         (clientPars.get(SSVarU.label));
        
        try{
          entities       = SSUri.get (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.entities), SSStrU.comma));
        }catch(Exception error){}
        
        try{
          users         = SSUri.get (SSStrU.splitDistinctWithoutEmptyAndNull(clientPars.get(SSVarU.users), SSStrU.comma));
        }catch(Exception error){}
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
