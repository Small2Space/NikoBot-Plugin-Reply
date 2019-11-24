package com.github.smallru8.plugin.reply;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.Embed;
import com.github.smallru8.NikoBot.event.Event.MessageEvent;
import com.github.smallru8.NikoBot.plugins.PluginsInterface;
import com.github.smallru8.plugin.reply.commands.ReplyCore;
import com.github.smallru8.plugin.reply.commands.ReplyMsgImg;
import com.github.smallru8.plugin.reply.commands.ReplyOnTag;
import com.github.smallru8.plugin.reply.commands.ReplyThread;

import net.dv8tion.jda.api.entities.Message;

public class Reply implements PluginsInterface{

	public static ArrayList<String[]> RplyCMDLs;
	/*
	 * type
	 * dir
	 * keyword(postfix)
	 * flag
	 * */
	private boolean replyFlag = true;
	private void loadCmdFromFile(String path) throws IOException {//預載指令
		String[] tmpLs = new File("reply/cmd/" + path).list();
		for(String fName:tmpLs) {
			File f = new File("reply/cmd/"+fName);
			if(f.isDirectory())
				loadCmdFromFile(fName+"/");
			else {
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String[] tmpSet = new String[4];
				tmpSet[0] = br.readLine();//img | msg
				tmpSet[1] = br.readLine();//dir
				tmpSet[2] = br.readLine();//keyword
				tmpSet[3] = br.readLine();//continue | break
				if(tmpSet[0]!=null&&tmpSet[1]!=null&&tmpSet[2]!=null&&tmpSet[3]!=null) {
					RplyCMDLs.add(tmpSet);
					System.out.println("[LOAD][PLUGIN][REPLY]:CMD file " + fName + " has been loaded.");
				}else {//error
					System.out.println("[WARN][PLUGIN][REPLY]:CMD file " + fName + ",loading error. Ignore this file.");
				}
				br.close();
				fr.close();
			}
		}
	}
	private Queue<String> ToPostfix(String str){
		int l=0,r=0;
		for(int i=0;i<str.length();i++) {
			if(str.charAt(i)=='(')
				l++;
			else if(str.charAt(i)==')')
				r++;
		}
		if(r==l) {
			Stack<String> tmpSta = new Stack<String>();
			Queue<String> reQ = new LinkedList<String>();
			String tmp = "",s = str;
			tmp = s.replaceAll(" ", "");//去除空白
			s = tmp;
			while(!s.isEmpty()) {
				if(s.startsWith("(")) {
					tmpSta.push("(");
					tmp = s.replaceFirst("\\(", "");
				}else if(s.startsWith(")")) {
					String stmp = "";
					while(!(stmp=tmpSta.pop()).equals("("))
						reQ.add(stmp);
					tmp = s.replaceFirst("\\)", "");
				}else if(s.startsWith("&&")||s.startsWith("||")) {
					if((!tmpSta.isEmpty())&&(tmpSta.peek().equals("&&")||tmpSta.peek().equals("||")))
						reQ.add(tmpSta.pop());
					tmpSta.push(s.substring(0, 2));
					if(s.startsWith("&&"))
						tmp = s.replaceFirst("&&", "");
					else
						tmp = s.replaceFirst("\\|\\|", "");
				}else {
					int endIndex = s.length();
					if(s.indexOf("&&")!=-1)
						endIndex = s.indexOf("&&");
					if(s.indexOf("||")!=-1)
						endIndex = Math.min(endIndex,s.indexOf("||"));
					if(s.indexOf(")")!=-1)
						endIndex = Math.min(endIndex,s.indexOf(")"));
					reQ.add(s.substring(0, endIndex));
					tmp = s.replaceFirst(s.substring(0, endIndex), "");
				}
				s = tmp;		
			}
			while(!tmpSta.isEmpty())
				reQ.add(tmpSta.pop());
			return reQ;
		}
		return null;
	}
	public void onDisable() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
	}

	public void onEnable() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		try {
			new CfgCreater();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RplyCMDLs = new ArrayList<String[]>();
		try {
			loadCmdFromFile("");
			for(int i=0;i<RplyCMDLs.size();i++) {
				Queue<String> sq = ToPostfix(RplyCMDLs.get(i)[2]);
				if(sq == null) {
					System.out.println("[ERROR][PLUGIN][REPLY]:TYPE:"+RplyCMDLs.get(i)[0]+",DIR:"+RplyCMDLs.get(i)[1]+",Its cmd file has a syntax error.");
					RplyCMDLs.remove(i);
					continue;
				}
				RplyCMDLs.get(i)[2] = "";
				while(!sq.isEmpty())
					RplyCMDLs.get(i)[2]+=(sq.poll() + " ");
				sq.clear();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String pluginsName() {
		// TODO Auto-generated method stub
		return "ReplyCore";
	}
	
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageRecved(MessageEvent e) {
		Message msg = e.msg;
		if(!msg.getAuthor().isBot()) {
			if(msg.getContentRaw().split(" ")[0].equalsIgnoreCase("/replyreload")&&Core.PM.userId(msg)) {
				Embed.EmbedSender(Color.yellow, msg.getChannel(), ":arrows_counterclockwise: Reloading reply list.", "");
				try {
					RplyCMDLs.clear();
					loadCmdFromFile("");
					for(int i=0;i<RplyCMDLs.size();i++) {
						Queue<String> sq = ToPostfix(RplyCMDLs.get(i)[2]);
						if(sq == null) {
							System.out.println("[ERROR][PLUGIN][REPLY]:TYPE:"+RplyCMDLs.get(i)[0]+",DIR:"+RplyCMDLs.get(i)[1]+",Its cmd file has a syntax error.");
							RplyCMDLs.remove(i);
							continue;
						}
						RplyCMDLs.get(i)[2] = "";
						while(!sq.isEmpty())
							RplyCMDLs.get(i)[2]+=(sq.poll() + " ");
						sq.clear();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Embed.EmbedSender(Color.yellow, msg.getChannel(), ":ballot_box_with_check: Reply list has been reloaded.", "");
			}else if(msg.getContentRaw().split(" ")[0].equalsIgnoreCase("/reply")) {
				if(msg.getContentRaw().split(" ")[1].startsWith("on")) {
					replyFlag = true;
					Embed.EmbedSender(Color.green, msg.getChannel(), ":speech_balloon: Auto reply on.", "");
				}else if(msg.getContentRaw().split(" ")[1].startsWith("off")) {
					replyFlag = false;
					Embed.EmbedSender(Color.red, msg.getChannel(), ":speech_balloon: Auto reply off.", "");
				}
			}else if(replyFlag){//處理訊息分析
				ReplyCore tag = new ReplyOnTag(msg);
				Thread tag_t = new ReplyThread(tag);
				tag_t.start();
				ReplyCore MsgImg = new ReplyMsgImg(msg);
				Thread MsgImg_t = new ReplyThread(MsgImg);
				MsgImg_t.start();
			}
		}
	}
}
