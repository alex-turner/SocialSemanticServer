package at.kc.tugraz.ss.recomm.impl.file;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.ss.recomm.impl.common.UserData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PredictionFileWriter {

	private static final int OUTPUT_LIMIT = 10;
	
	private WikipediaReader reader;
	private List<int[]> results;
	
	public PredictionFileWriter(WikipediaReader reader, List<int[]> results) {
		this.reader = reader;
		this.results = results;
	}
	
	public boolean writeFile(String filename) {
		List<String> categories = this.reader.getTags();
		
		try {
			FileWriter writer = new FileWriter(new File("./data/results/" + filename + ".txt"));
			BufferedWriter bw = new BufferedWriter(writer);

			for (int i = 0; i < this.results.size(); i++) {
				int j = 0;
				String resultString = "";
				int[] userResults = this.results.get(i);
				    UserData userData = this.reader.getUserLines().get(i);
				List<Integer> userCats = userData.getTags();
				
				resultString += (this.reader.getUsers().get(userData.getUserID()) +
						(userData.getWikiID() == -1 ? "" : "-" + this.reader.getResources().get(userData.getWikiID())) + "|");
				for (int c : userCats) {
					//if (j++ < OUTPUT_LIMIT) {
						//resultString += (categories.get(c) + ", ");
						resultString += (c + ", ");
					//} else {
					//	break;
					//}
				}
				if (userCats.size() > 0) {
					resultString = resultString.substring(0, resultString.length() - 2);
				}
				resultString += "|";
				
				j = 0;
				for (int c : userResults) {
					if (j++ < OUTPUT_LIMIT) {
						//resultString += (categories.get(c) + ", ");
						resultString += (c + ", ");
					} else {
						break;
					}
				}
				if (userResults.length > 0) {
					resultString = resultString.substring(0, resultString.length() - 2);
				}
				resultString += "\n";
		
				bw.write(resultString);
			}
			
			bw.flush();
			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean writeResourcePredictionsToFile(String filename, int trainSize, int neighborSize) {
		List<String> resourceList = this.reader.getResources();
		Map<Integer, List<Integer>> resourcesOfTestUsers = this.reader.getResourcesOfTestUsers(trainSize);
		
		try {
			FileWriter writer = new FileWriter(new File("./data/results/" + filename + ".txt"));
			BufferedWriter bw = new BufferedWriter(writer);
						
			int correctResourceRecommendation = 0;
			int noMatchOfResources = 0;
			List<Integer> positions = new ArrayList<>();
			
			int i=0;
			String helpString = "";
			for (Integer userID : reader.getUniqueUserListFromTestSet(trainSize)) {
				String resultString = (this.reader.getUsers().get(userID) + "-XYZ|");
				
				String givenResourcesOfUser = "";
								
				for (Integer resourceID : resourcesOfTestUsers.get(userID)) {
					givenResourcesOfUser += /*resourceList.get(*/resourceID/*)*/ + ", ";
				}
				
				if (givenResourcesOfUser != "") {
					givenResourcesOfUser = givenResourcesOfUser.substring(0, givenResourcesOfUser.length() - 2);
				}
				
				resultString += givenResourcesOfUser + "|";
				
				
				int[] recommendedResources = this.results.get(i);
				String recString = "";
				int cnt=0;

				for (int recResourceID : recommendedResources) {
				
					if (cnt++ < 10) {
						recString += /*resourceList.get(*/recResourceID/*)*/ + ", ";
						if (resourcesOfTestUsers.get(userID).contains(recResourceID)) {
							helpString += this.reader.getUsers().get(userID) + " " + resourceList.get(recResourceID) + ", ";
						}
					} else {
						break;
					}
					
					/*if (resourcesOfUsers.get(userID).contains(recResourceID)) {
						correctResourceRecommendation++;
						positions.add(cnt);
					}*/
				}
				
				if (recString.equals("")) {
          SSLogU.info("no recommended resources for user " + userID + " " + this.reader.getUsers().get(userID));
					noMatchOfResources++;
				} else {
					recString = recString.substring(0, recString.length() - 2);
				}
				
				resultString += recString + "\n";
				helpString += "\n";
				bw.write(resultString);
				i++;
			}
			
			bw.flush();
			bw.close();
			writer.close();
			
			SSLogU.info("total user: " + results.size());
			SSLogU.info("number of users without any matching recommendation: " + noMatchOfResources);
			SSLogU.info("total correct recommendations: " + correctResourceRecommendation);
			
			writer = new FileWriter(new File("./data/results/" + filename + "_positions" + (neighborSize != -1 ? ("_" + neighborSize) : "" ) + ".txt"));
			bw = new BufferedWriter(writer);
			
			//String recPositions = positions.toString().substring(1, positions.toString().length()-1);
			//bw.write(recPositions);
			
			bw.write(helpString);
			
			bw.flush();
			bw.close();
						
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
