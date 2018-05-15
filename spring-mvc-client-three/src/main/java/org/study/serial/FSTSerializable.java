package org.study.serial;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.nustaq.serialization.FSTObjectOutput;

/**
 * @Title: FSTSerializable.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午6:50:11
 * @see {@linkplain https://my.oschina.net/changety/blog/197627}
 */
public class FSTSerializable {
	public static boolean saveObjectByJava(Object o, String filename) {
		FSTObjectOutput oos = null;
		// ObjectOutputStream oos = null;
		try {
			oos = new FSTObjectOutput(new FileOutputStream(filename));
			oos.writeObject(o);
			oos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static Object readObjectByJava(String filename) {
		// FSTObjectInput ois = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(filename));
			return ois.readObject();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
