import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import iceworld.given.*;

import org.json.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class ICEWorldStates {

  CheckConnection ice = new CheckConnection();
	LinkedList<LoginSever> userList;
	LinkedList<MyIcetizenAction> actionList;
	String condition;
	long last_change;
	long current_time;
	long latestActionTime;
	public ICEWorldStates(){
		latestActionTime = current_time;
	}
	public long getTime(){
		return current_time;
	}
	
	public String getWeatherCondition(){
		return condition;
	}
	
	public long getWeatherLastChange(){
		return last_change;
	}
	
	public LinkedList<LoginSever> getUserList(){
		return userList;
	}

	public LinkedList<MyIcetizenAction> getAction(){
		return actionList;
	}
	public void setTime() throws MalformedURLException{
		String time = ice.getRequest(new URL("http://iceworld.sls-atl.com/api/&cmd=time"));
		JSONObject jtime = (JSONObject)JSONValue.parse(time);
		current_time = Long.parseLong((String)jtime.get("data"));
	}
	public void setWeather() throws MalformedURLException{
		String states = ice.getRequest(new URL("http://iceworld.sls-atl.com/api/&cmd=states"));
		JSONObject jstates = (JSONObject) JSONValue.parse(states);
		JSONObject data = (JSONObject) jstates.get("data");
		JSONObject weather = (JSONObject) data.get("weather");
		condition =  weather.get("condition").toString();
		//System.out.println("Weather condition:"+condition);
		last_change = (Long)weather.get("last_change");
		//System.out.println("Last Weather Change:"+last_change);
	}
	public void setUser() throws MalformedURLException{
		String states = ice.getRequest(new URL("http://iceworld.sls-atl.com/api/&cmd=states"));
		JSONObject jstates = (JSONObject) JSONValue.parse(states);
		JSONObject data = (JSONObject) jstates.get("data");
		JSONObject icetizen = (JSONObject) data.get("icetizen");
		userList= new LinkedList<LoginSever>();
		Set keys = icetizen.keySet();
		for(Object key: keys){
			JSONObject userid = (JSONObject) icetizen.get(key);
			JSONObject last_known_destination = (JSONObject) userid.get("last_known_destination");
			try{
				Point position;
				if(last_known_destination.get("position")!=(null)){
					String stringPosition = (String) last_known_destination.get("position");
					int beginIndexX = 1;
					int endIndexX = stringPosition.indexOf(",");
					int beginIndexY = endIndexX+1;
					int endIndexY = stringPosition.indexOf(")");
					int x = Integer.parseInt(stringPosition.substring(beginIndexX, endIndexX));
					int y = Integer.parseInt(stringPosition.substring(beginIndexY, endIndexY));
					position = new Point(x , y);
				}else{
					position = new Point(0,0);
				}
				long timestamp;
				if(last_known_destination.get("timestamp")!=(null)){
					timestamp = Long.parseLong(""+last_known_destination.get("timestamp"));
				}else{
					timestamp = -1;
				}
				JSONObject user = (JSONObject) userid.get("user");
				int uid = Integer.parseInt((String)key);
				String username = (String) user.get("username");
				long type = (Long) user.get("type");
				String ip = (String) user.get("ip");
				int pid;
				int port;
				if(username.equals("SivaGod")||username.equals("EtherealProgrammer")){
					pid = 0;
					port = 0;
				}else{
					port =  Integer.parseInt((String)user.get("port"));
					pid =  Integer.parseInt((String)user.get("pid"));
				}
				
				System.out.println(timestamp);
				System.out.println(position);
				System.out.println(uid);
				System.out.println(username);
				System.out.println(type);
				System.out.println(ip);
				System.out.println(port);
				System.out.println(pid);
				System.out.println("==========================");
				LoginSever myIcetizen = new LoginSever();
				myIcetizen.setIcePortID(pid);
				myIcetizen.setIcetizenLook(getLook(uid));
				myIcetizen.setListeningPort(port);
				myIcetizen.setUsername(username);
				myIcetizen.setUserID(uid);
				myIcetizen.setIPAdress(ip);
				myIcetizen.setType(type);
				myIcetizen.setPosition(position);
				myIcetizen.setTimestamp(timestamp);
				userList.add(myIcetizen);
				//}else{
				//}
			}catch(Exception e){
				//System.out.println();
				System.out.println("Null Position or Null Timestamp");
			}
		}
	}
	public static void main(String[] args) throws MalformedURLException{
		ICEWorldStates state = new ICEWorldStates();
		state.setUser();
	}
	public IcetizenLook getLook(int uid) throws MalformedURLException{
		String looks = ice.getRequest(new URL("http://iceworld.sls-atl.com/api/&cmd=gresources&uid="+uid));
		JSONObject jlooks = (JSONObject) JSONValue.parse(looks);
		JSONArray d = (JSONArray) jlooks.get("data");
		JSONObject data = (JSONObject) d.get(0);
		IcetizenLook f = new IcetizenLook();
		f.gidB = (String) data.get("B");
		f.gidH =(String) data.get("H");
		f.gidS = (String) data.get("S");
		f.gidW = (String) data.get("W");

		//System.out.println("B: "+f.gidB+"   H: "+f.gidH+"   S:"+f.gidS+"    W:"+f.gidW);
		return f;
	}
	public int getUserID(String userUsername){
		int uid=0;
		/*for(MyIcetizenP3V uuu : userList){
			if(uuu.getUsername().equals(userUsername)){
				uid
			}
		}*/
		for(int i=0; i<userList.size();i++){
			if(userList.get(i).username.equals(userUsername)){
				uid=userList.get(i).userID;
			}
		}
		return uid;
	}
	public ArrayList<String> getAliensUsername(){
		ArrayList<String> a = new ArrayList<String>();
		for(int i=0;i<userList.size();i++){
			if(userList.get(i).type==0 && userList.get(i).IcePortID==250){
				a.add(userList.get(i).username);
			}
		}
		return a;
	}
	public String getAlienUsername(){
		String a = "";
		for(int i=0;i<userList.size();i++){
			if(userList.get(i).type==0 && userList.get(i).IcePortID==250){
				a = userList.get(i).username;
			}
		}
		return a;
	}

	public String getLink(String s) throws MalformedURLException{
		String g = ice.getRequest(new URL("http://iceworld.sls-atl.com/api/&cmd=gurl&gid="+s));
		JSONObject jLink = (JSONObject) JSONValue.parse(g);
		JSONObject data = (JSONObject) jLink.get("data");
		String address = (String) data.get("location");
		System.out.println(address);
		return address;
	}
	public BufferedImage getPic(IcetizenLook look) throws IOException{
		if(look.gidB==null && look.gidH==null && look.gidS==null && look.gidW==null){
			look.gidB = "B007";
			look.gidH = "H015";
			look.gidS = "S040";
			look.gidW = "W124";
		}
		BufferedImage b = ImageIO.read(new URL("http://iceworld.sls-atl.com/"+getLink(look.gidB)));
		BufferedImage h = ImageIO.read(new URL("http://iceworld.sls-atl.com/"+getLink(look.gidH)));
		BufferedImage s = ImageIO.read(new URL("http://iceworld.sls-atl.com/"+getLink(look.gidS)));
		BufferedImage w = ImageIO.read(new URL("http://iceworld.sls-atl.com/"+getLink(look.gidW)));

		BufferedImage avatar = new BufferedImage(1000,1000,BufferedImage.TYPE_INT_ARGB);
		Graphics g = avatar.getGraphics();
		g.drawImage(b, 0, 0, null);
		g.drawImage(h, 0, 0, null);
		g.drawImage(s, 0, 0, null);
		g.drawImage(w, 0, 0, null);
		return avatar;
	}
	public void setAction(long time) throws MalformedURLException{
		String actions = ice.getRequest(new URL("http://iceworld.sls-atl.com/api/&cmd=actions&from="+time));
		JSONObject jaction = (JSONObject) JSONValue.parse(actions);
		JSONArray d = (JSONArray) jaction.get("data");
		//JSONObject data = (JSONObject) d.get(0);
		actionList = new LinkedList<MyIcetizenAction>();
		JSONObject data;
		for(int i =0 ;i<d.size();i++){
			data = (JSONObject) d.get(i);
			long timestamp = Long.parseLong((String)data.get("timestamp"));
			//System.out.println(timestamp);
			int uid = Integer.parseInt((String)data.get("uid"));
			//System.out.println(uid);
			int action_type = Integer.parseInt((String)data.get("action_type"));
			//System.out.println(action_type);
			int aid = Integer.parseInt((String)data.get("aid"));
			//System.out.println(aid);
			Point position = new Point();
			String chat = "";
			if(action_type==1){
				String positionString = (String)data.get("detail");
				int positionX = Integer.parseInt(positionString.substring(1,positionString.indexOf(",")));
				int positionY = Integer.parseInt(positionString.substring(positionString.indexOf(",")+1,positionString.indexOf(")")));
				position = new Point(positionX,positionY);
				//System.out.println(position);
			}else{
				chat = (String)data.get("datail");
				//System.out.println(chat);
			}
			//System.out.println("--------------------------");
			MyIcetizenAction action = new MyIcetizenAction();
			action.setTimestamp(timestamp);
			if(action_type==1){
				action.setWalkDetail(position);
			}else{
				action.setTalkYellDetail(chat);
			}
			action.setActionType(action_type);
			action.setActionID(aid);
			actionList.add(action);
		}}}
