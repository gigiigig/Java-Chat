package gg.msn.facebook.core;


public class FacebookMessage {
	/*{"text":"FINE",
	"time":1214614165139,
	"clientTime":1214614163774,
	"msgID":"1809311570"},
	"from":1190346972,
	"to":1386786477,
	"from_name":"David Willer",
	"to_name":"\u5341\u4e00",
	"from_first_name":"David",
	"to_first_name":"\u4e00"}*/
	public String text;
	public Number time;
	public Number clientTime;
	public String msgID;
	public Number from;
	public Number to;
	public String fromName;
	public String toName;
	public String fromFirstName;
	public String toFirstName;
	public String type;
	
	public FacebookMessage(){}
	
	public FacebookMessage(String txt, Number tm, Number ct, String id, Number f, Number t, String fn, String tn, String ffn, String tfn){
		text = txt;
		time = tm;
		clientTime = ct;
		msgID = id;
		from = f;
		to = t;
		fromName = fn;
		toName = tn;
		fromFirstName = ffn;
		toFirstName = tfn;
	}
}