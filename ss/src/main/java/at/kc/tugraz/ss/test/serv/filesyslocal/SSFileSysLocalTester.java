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
package at.kc.tugraz.ss.test.serv.filesyslocal;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.serv.job.file.sys.local.conf.SSFileSysLocalConf;
import at.kc.tugraz.ss.serv.job.file.sys.local.serv.SSFileSysLocalServ;

public class SSFileSysLocalTester{
  
  public static void run() throws Exception{
    
    SSFileSysLocalConf fileSysLocalConf   = (SSFileSysLocalConf) SSFileSysLocalServ.inst.servConf;
    
    if(!fileSysLocalConf.executeOpAtStartUp){
      return;
    }
    
    if(SSMethU.equals(fileSysLocalConf.op, SSMethU.fileSysLocalFormatAudioAndVideoFileNamesInDir)){
      new Thread(new SSFileSysLocalFormatAudioAndVideoFileNamesInDirTest(fileSysLocalConf)).start();
    }
    
    if(SSMethU.equals(fileSysLocalConf.op, SSMethU.fileSysLocalAddTextToFilesNamesAtBeginInDir)){
      new Thread(new SSFileSysLocalAddTextToFilesNamesAtBeginInDirTest(fileSysLocalConf)).start();
    }
  }
}
