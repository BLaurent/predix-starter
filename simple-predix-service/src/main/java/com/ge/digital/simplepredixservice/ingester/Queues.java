/*******************************************************************************
 * Copyright 2016 General Electric Company.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.ge.digital.simplepredixservice.ingester;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Cross Thead Chat object holding all the que
 */
class Queues {
	private final int MAX_BYTE_QUEUE_SIZE = 1000;
	private final int MAX_FILE_QUEUE_SIZE = 10;
	private final int MAX_DATAPOINT_QUEUE_SIZE = 1000;

	private Queue<TaggedDataPoint> pointQueue;
	private Queue<byte[]> byteQueue;
	private Queue<String> fileQueue;

	private boolean taggedError, byteError, fileError;

	/**
	 * Chat object containg a queue Each Queue has a Push, pop and size.
	 *
	 * Tagged Datapont: SoundProcessor and network pushes to it while
	 * DataIngester pops it
	 *
	 * byte: SoundProcessor pushes byte wavewriter pops
	 *
	 * File: wavewriter and camera taker pushes while blobuploader pops
	 */
	Queues() {
		super();
		pointQueue = new ConcurrentLinkedQueue<>();
		byteQueue = new ConcurrentLinkedQueue<>();
		fileQueue = new ConcurrentLinkedQueue<>();

		taggedError = false;
		byteError = false;
		fileError = false;
	}

	void pushDataPoint(TaggedDataPoint p) {
		if (pointQueue.size() > MAX_DATAPOINT_QUEUE_SIZE) {
			if (!taggedError) {
				System.out.println("Point Queue too large... ingest froze");
				taggedError = true;
			}
		} else {
			taggedError = false;
			pointQueue.offer(p);
		}
	}

	int getPointQueueSize() {
		return pointQueue.size();
	}

	TaggedDataPoint popDataPoint() {
		return pointQueue.remove();
	}

	public void pushByteArray(byte[] b) {
		if (byteQueue.size() > MAX_BYTE_QUEUE_SIZE) {
			if (!byteError) {
				System.out.println("Byte Queue too large... saving froze");
				byteError = true;
			}
		} else {
			byteError = false;
			byte[] newByte = new byte[b.length];
			System.arraycopy(b, 0, newByte, 0, b.length);
			byteQueue.offer(newByte);
		}
	}

	public int getByteQueueSize() {
		return byteQueue.size();
	}

	public byte[] popByte() {
		return byteQueue.remove();
	}

	public void pushFile(String file) {
		if (fileQueue.size() > MAX_FILE_QUEUE_SIZE) {
			if (!fileError) {
				System.out.println("File Queue too large... Blobstore upload froze");
				fileError = true;
			}
		} else {
			fileError = false;
			fileQueue.offer(file);
		}
	}
	public int getFileQueueSize() {
		return fileQueue.size();
	}
	public String popFile() {
		return fileQueue.remove();
	}

}
