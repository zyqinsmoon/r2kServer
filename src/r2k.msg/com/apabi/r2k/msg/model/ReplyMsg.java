package com.apabi.r2k.msg.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReplyMsg {
	public static List<R2kMessage> replayMsgList = Collections.synchronizedList(new ArrayList<R2kMessage>());


}
