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
package at.kc.tugraz.ss.serv.job.i5cloud.datatypes;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import java.util.ArrayList;
import java.util.List;

public class SSi5CloudAchsoVideo extends SSEntityA{
  
  public SSUri        id            = null;
  public SSLabel      label          = null;
  public SSLabel      author         = null;
  public Long         creationTime   = null;
  public List<String> keywords       = new ArrayList<String>();
  public List<String> annotations    = new ArrayList<String>();
  
  public static SSi5CloudAchsoVideo get(
    final SSLabel      title,
    final SSUri        uri,
    final SSLabel      author,
    final Long         creationTime,
    final List<String> keywords,
    final List<String> annotations) throws Exception{
    
    return new SSi5CloudAchsoVideo(
      title, 
      uri,
      author, 
      creationTime, 
      keywords, 
      annotations);
  }
  
  private SSi5CloudAchsoVideo(
    final SSLabel      label,
    final SSUri        uri,
    final SSLabel      author,
    final Long         creationTime,
    final List<String> keywords,
    final List<String> annotations) throws Exception{
    
    super(uri);
    
    this.label        = label;
    this.id          = uri;
    this.author       = author;
    this.creationTime = creationTime;
    
    if(keywords != null){
      this.keywords.addAll(keywords);
    }
    
    if(annotations != null){
      this.annotations.addAll(annotations);
    }
  }

  @Override
  public Object jsonLDDesc(){
    return null;
  }
}
