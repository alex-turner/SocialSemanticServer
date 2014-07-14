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

package at.kc.tugraz.ss.serv.datatypes.entity.impl.fct.activity;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.activity.datatypes.enums.SSActivityE;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCircleCreatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserCopyPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserEntitiesToCircleAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserPublicSetPar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserSharePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserUpdatePar;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUserUsersToCircleAddPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.serv.serv.datatypes.err.SSServerServNotAvailableErr;
import java.util.ArrayList;
import java.util.List;

public class SSEntityActivityFct{
  
  public static void shareEntityWithUsers(
    final SSEntityUserSharePar par) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.shareEntityWithUsers,
        par.users,
        SSUri.asListWithoutNullAndEmpty(par.entity),
        SSTextComment.asListWithoutNullAndEmpty(par.comment),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void copyEntityForUsers(
    final SSEntityUserCopyPar par) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.copyEntityForUsers,
        par.users,
        SSUri.asListWithoutNullAndEmpty(par.entity),
        SSTextComment.asListWithoutNullAndEmpty(par.comment),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void entityUpdate(
    final SSEntityUserUpdatePar par) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.updateEntity,
        new ArrayList<>(),
        SSUri.asListWithoutNullAndEmpty(par.entity),
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void createCircle(
    final SSEntityUserCircleCreatePar par,
    final SSUri                       circle) throws Exception{
    
    try{
      
      final List<SSUri> eventEntities = new ArrayList<>();
      
      eventEntities.addAll (par.entities);
      eventEntities.add    (circle);
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.createCircle,
        par.users,
        eventEntities,
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void addEntitiesToCircle(
    final SSEntityUserEntitiesToCircleAddPar par) throws Exception{
    
    try{
      
      final List<SSUri> eventEntities = new ArrayList<>();
      
      eventEntities.addAll (par.entities);
      eventEntities.add    (par.circle);
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addEntitiesToCircle,
        new ArrayList<>(),
        eventEntities,
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void addUsersToCircle(
    final SSEntityUserUsersToCircleAddPar par) throws Exception{
    
    try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.addUsersToCircle,
        par.users,
        SSUri.asListWithoutNullAndEmpty(par.circle),
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void setEntityPublic(
    final SSEntityUserPublicSetPar par) throws Exception{
    
     try{
      
      SSServCaller.activityAdd(
        par.user,
        SSActivityE.setEntityPublic,
        new ArrayList<>(),
        SSUri.asListWithoutNullAndEmpty(par.entity),
        SSTextComment.asListWithoutNullAndEmpty(),
        false);
      
    }catch(SSServerServNotAvailableErr error){
      SSLogU.warn("activityAdd failed | service down");
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}