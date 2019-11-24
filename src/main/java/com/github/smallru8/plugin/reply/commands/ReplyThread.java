package com.github.smallru8.plugin.reply.commands;

public class ReplyThread extends Thread{
	ReplyCore rc;
	public void run() {
		if(rc.analysisMsg())
			rc.sendToDC();
	}
	public ReplyThread(ReplyCore rc) {
		this.rc = rc;
	}
}
