package com.github.smallru8.plugin.reply.commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.github.smallru8.NikoBot.Core;

import net.dv8tion.jda.api.entities.Message;

public class ReplyOnTag extends ReplyCore{

	public ReplyOnTag(Message msg) {
		super(msg);
	}

	@Override
	public boolean analysisMsg() {
		if(recvStr.contains(Core.botAPI.getSelfUser().getId())) {
			try {
				FileReader fr = new FileReader("reply/OnTag.yml");
				BufferedReader br = new BufferedReader(fr);
				ArrayList<String> replyTmp = new ArrayList<String>();
				String tmp = null;
				while((tmp=br.readLine())!=null)
					replyTmp.add(tmp);
				br.close();
				fr.close();
				reply = replyTmp.get(randomInt(replyTmp.size()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
