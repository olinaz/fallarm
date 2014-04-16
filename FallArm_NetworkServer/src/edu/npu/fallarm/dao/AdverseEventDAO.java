package edu.npu.fallarm.dao;

import edu.npu.fallarmserver.MessageParser;

public interface AdverseEventDAO {
	public void createAdverseEvent(MessageParser msg);
}
