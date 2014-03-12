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
package at.kc.tugraz.socialserver.utils;

public class SSObjU {
  
  private SSObjU(){}
  
	public static Boolean isNotNull(final Object object){
		
		if(object != null){
			return true;
		}
		
		return false;
	}
  
  public static Boolean isNull(final Object... objs) {
    
    for(Object obj : objs){
      
      if(obj == null){
        return true;
      }
    }
    
    return false;
  }
	
	public static Boolean isNull(final Object obj){
		return obj == null;
	}
}