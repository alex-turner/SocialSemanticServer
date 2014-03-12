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

import java.io.*;
import java.nio.charset.Charset;

public class SSFileU{
   
//  public static final String folderTemp                            = "./kbw-ss/";
  public static final String folderConf                            = "conf/";
  public static final String fileNameWithExtSSConf                 = "ss-conf.yaml";
  public static final String fileNameWithExtSSAdapterRestConf      = "ss-adapter-rest-conf.yaml";
  public static final String fileNameWithExtSSAdapterWebSocketConf = "ss-adapter-websocket-conf.yaml";

  private SSFileU(){}
  
  public static String dirUserHome() {
		return correctDirPath(System.getProperty("user.home"));
	}
  
  public static String dirUser() {
		return correctDirPath(System.getProperty("user.dir"));
	}
    
  public static String dirWorking(){
    return correctDirPath(new File(SSStrU.empty).getAbsolutePath());
  }
  
  public static String dirCatalinaHome(){
    return correctDirPath(System.getProperty("catalina.home"));
  }
  
  public static String dirCatalinaBase(){
    return correctDirPath(System.getProperty("catalina.base"));
  }
  
  public static String dirWorkingTmp() {
    return dirWorking() + "tmp/";
  }
    
  public static String dirWorkingData(){
    return dirWorking() + "data/";
  }
  
  public static String dirWorkingDataCsv(){
    return dirWorking() + "data/csv/";
  }
  
  public static String dirWorkingScriptR(){
    return dirWorking() + "script/r/";
  }
  
	/**
	 * <ul>
	 * <li>if folder is null, folder will be set to an empty String</li>
	 * <li>replaces all "\" w/ "/"</li>
	 * </ul>
	 * @param folder
	 * @return the input parameter if folder already ends with "/" or is empty, otherwise
	 * "/" is appended
	 */
	public static String correctDirPath(String folder) {
		
    if (SSStrU.isEmpty(folder)){
			folder = SSStrU.empty;
		}
    
		folder = SSStrU.replace(folder, SSStrU.backSlash, SSStrU.slash);
    
    if(
      folder.endsWith      (SSStrU.slash) ||
      SSStrU.isEmpty(folder)){

      return folder;
    }else{
      return folder + SSStrU.slash;
    }
	}
  
	public static void delFile(String filePath) throws Exception {
		
    try {
			File file = new File(filePath);
			file.delete();
		}catch (Exception error){
      SSLogU.errThrow(error);
		}
	}
  
  public static void appendTextToFile(String filePath, String text) throws Exception{
		
		FileWriter     fileWriter    = null;
		BufferedWriter bufferWritter = null;
		
		try {
			fileWriter    = new FileWriter     (filePath, true);
			bufferWritter = new BufferedWriter (fileWriter); 
			
			bufferWritter.write(text);
        
		}catch (IOException error) {
			SSLogU.errThrow(error);
		}finally{
			
			if(bufferWritter != null){
				bufferWritter.close();
			}
			
			if(fileWriter!= null){
				fileWriter.close();
			}
		}
	}	
	
	public static FileInputStream openFileForRead(String filePath) throws Exception{

		try{
			return new FileInputStream(new File(filePath));
		}catch (Exception error) {
			SSLogU.errThrow(error);
      return null;
		}
	}
	
	public static FileOutputStream openOrCreateFileWithPathForWrite(final String filePath) throws Exception{
		
		try{
      
      final File file = new File(filePath);
      
      if(!file.exists()){
        file.getParentFile().mkdirs();
      }

      return new FileOutputStream(file);
    }catch(Exception error){
      SSLogU.errThrow(error);
      return null;
    }
	}
  
  public static String readFileText(File file, Charset charset) throws Exception {
   
    FileInputStream   in          = openFileForRead(file.getAbsolutePath());
    String            fileContent = SSStrU.empty;
    byte[]            bytes       = new byte[1];
    
    while(in.read(bytes) != -1){
      fileContent += new String(bytes, charset);
    }
    
    in.close();
    
    return fileContent;
  }
  
  public static void writeFileText(File file, String text) throws Exception{
    
    FileOutputStream openFileForWrite = openOrCreateFileWithPathForWrite(file.getAbsolutePath());
    byte[]           bytes            = text.getBytes();
    
    openFileForWrite.write (bytes, 0, bytes.length);
    openFileForWrite.close ();
  }
  
  public static File[] filesForDirPath(String dirPath){
    return new File(correctDirPath(dirPath)).listFiles();
  }
}

//  private static void existsDirTemp() throws Exception{
//  
//    File file = new File(dirWorkingTmp());
//		
//    if(
//      file.exists() && 
//      file.isDirectory()) {
//			return;
//		}
//    
//		if(!file.mkdir()) {
//			SSLogU.logAndThrow(new Exception("temp folder doesn't exists neither could be created"));
//		}
//  }

//public static boolean isUrlReachable(String url) {
//    try {
//      InetAddress address = InetAddress.getByName(url);
//      return address.isReachable(1000);
//    } catch (Exception e) {
//      log.debug(e.getMessage());
//    }
//    return false;
//  }

//	public static String copyDocumentFromWebdav(String filename) throws Exception {
//		try {
//			InputStream is = WebdavHelper.getInstance().getDocument(filename);
//			String location = SolrG.getTempFolderAbsolute();
//			SSUtils.copyFileFromInputStreamToLocal(is, location, filename);
//			return location;
//		} catch (Exception e) {
//			throw e;
//		}
//	}

///**
//	 * public static for testing only
//	 * @param url
//	 * @return
//	 */
//	public static String checkAndRepairSolrUrl(final String url) throws SolRException {
//		if (url == null || url.trim().isEmpty()) {
//			String msg = "no url for solr defined.";
//			log.error(msg);
//			throw new SolRException(msg);
//		}
//		String ret = url.startsWith(strU.PREFIX_HTTP) ? url : strU.PREFIX_HTTP + url;
//		return ret;
//	}

//  public static String getAsCorrectFolder(
//    String folder) {
//		
//		if (SSObjectUtils.isNull(folder)) {
//			folder = strU.strEmpty;
//		}
//		
//		folder = folder.replace("\\", "/");
//		
//		if(
//				folder.endsWith("/") ||
//				SSStrU.isEmpty(folder)){
//			
//			return folder;
//		}
//		
//		return folder + "/";
//	}