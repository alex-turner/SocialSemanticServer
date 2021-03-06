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
package at.kc.tugraz.ss.recomm.serv.task;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.api.SSServImplStartA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.util.TimerTask;

public class SSRecommTagsUpdateTask extends TimerTask {
  
  @Override
  public void run(){
    
    try{
      new Thread(new SSRecommTagsUpdater()).start();
    }catch(Exception error){
      SSServErrReg.regErr(error);
    }
  }
  
  protected class SSRecommTagsUpdater extends SSServImplStartA{
    
    public SSRecommTagsUpdater() throws Exception{
      super(null);
    }
    
    @Override
    public void run() {
      
      try{
        SSServCaller.recommTagsUpdate();
      }catch(Exception error1){
        SSServErrReg.regErr(error1);
      }finally{
        
        try{
          finalizeImpl();
        }catch(Exception error2){
          SSLogU.err(error2);
        }
      }
    }
    
    @Override
    protected void finalizeImpl() throws Exception{
      finalizeThread();
    }
  }
}