package org.study.serial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.print.attribute.standard.Media;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.hhcf.backend.model.UserModel;

/**
 * @Title: KryoTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午4:06:57
 * @see {@linkplain https://my.oschina.net/changety/blog/197627}
 */
public class KryoTest {

	public static void main(String[] args) {

	}

	private static Kryo myKryo = new Kryo();

	static {
		myKryo.register(UserModel.class, new CompatibleFieldSerializer<UserModel>(myKryo, Media.class), 50);
	}

	public static void saveObjectByKryo(Object o, String fileName) {
		Output output = null;
		try {
			output = new Output(new FileOutputStream(fileName), 8 * 1024);
			myKryo.writeObject(output, o);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	public static Object readObjectByKryo(String filename, Class<?> clazz) {
		Input input = null;
		try {
			input = new Input(new FileInputStream(new File(filename)));
			return myKryo.readObject(input, clazz);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (input != null) {
				input.close();
			}
		}
		return null;
	}

}
