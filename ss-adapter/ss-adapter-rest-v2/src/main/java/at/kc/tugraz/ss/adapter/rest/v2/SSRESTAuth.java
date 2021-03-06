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
package at.kc.tugraz.ss.adapter.rest.v2;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.rest.v2.pars.SSAuthCheckCredRESTAPIV2Par;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.pars.SSAuthCheckCredPar;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
@Api( value = "/auth", basePath = "/auth")
public class SSRESTAuth{
 
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/auth")
  @ApiOperation(
    value = "retrieve sss authentication token (i.e. key) and the user's uri for a given authtoken (e.g. OIDC authtoken) with header 'Authorization: Bearer yourToken'",
    response = SSAuthCheckCredRet.class)
  public Response checkCredWithAuthToken(
    @Context 
      HttpHeaders headers){

    try{
      
      return SSRestMainV2.handleGETRequest(
        headers,
        new SSAuthCheckCredPar(
          SSMethU.authCheckCred,
          null,
          null,
          null,
          null));
      
    }catch(Exception error){
      return Response.status(422).build();
    }
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/auth")
  @ApiOperation(
    value = "retrieve sss authentication token (i.e. key) and the user's uri for user name and password",
    response = SSAuthCheckCredRet.class)
  public Response checkCredWithUserNamePassword(
    final SSAuthCheckCredRESTAPIV2Par input){
    
    final SSAuthCheckCredPar par;
    
    try{
      
      par =
        new SSAuthCheckCredPar(
          SSMethU.authCheckCred,
          "1234",
          null,
          input.label,
          input.password);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.sendPOSTRequest(par);
  }
}