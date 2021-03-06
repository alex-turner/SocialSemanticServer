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
package at.kc.tugraz.ss.test.tag;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSSpaceE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.tag.conf.SSTagConf;
import at.kc.tugraz.ss.serv.test.api.SSServOpTestCaseA;
import at.kc.tugraz.ss.serv.voc.serv.SSVoc;
import at.kc.tugraz.ss.service.disc.datatypes.ret.SSDiscUserEntryAddRet;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SSTagsUserGetTest extends SSServOpTestCaseA{
  
  public SSTagsUserGetTest(final SSTagConf tagConf) {
    super(tagConf, SSMethU.tagsUserGet);
  }
  
  @Override
  protected void test() throws Exception {
    
    System.out.println (op + " test start");

    final Long tagAddTime = new Date().getTime();
    
    SSServCaller.tagsRemove(
      null, 
      null, 
      null, 
      null, 
      true);
    
    SSServCaller.tagAdd(
      SSVoc.systemUserUri, 
      SSUri.get("http://google.com"), 
      "super", 
      SSSpaceE.sharedSpace, 
      tagAddTime, 
      true);
    
    List<SSTag> tags = SSServCaller.tagsUserGet(
      SSVoc.systemUserUri, 
      null, 
      new ArrayList<>(), 
      new ArrayList<>(), 
      null, 
      tagAddTime);
    
    if(tags.isEmpty()){
      assert false;
    }
    
    tags = SSServCaller.tagsUserGet(
      SSVoc.systemUserUri, 
      null, 
      new ArrayList<>(), 
      new ArrayList<>(), 
      null, 
      new Date().getTime());

    if(!tags.isEmpty()){
      assert false;
    }
    
    System.out.println (op + " test end");
  }
  
  @Override
  protected void testFromClient() throws Exception{
    
  }
  
  @Override
  protected void setUp() throws Exception {
  }
}