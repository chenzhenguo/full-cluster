package org.study.serial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hhcf.backend.model.SimpleModel;

/**
 * @Title: KyroSerializable.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午4:18:10
 * @see {@linkplain https://www.cnblogs.com/520playboy/p/6341490.html}
 */
public class KyroSerializable {

	public static void main(String[] args) throws Exception {
		// long start = System.currentTimeMillis();
		// setSerializableObject();
		// System.out.println("Kryo 序列化时间:" + (System.currentTimeMillis() -
		// start) + " ms");
		// start = System.currentTimeMillis();
		// getSerializableObject();
		// System.out.println("Kryo 反序列化时间:" + (System.currentTimeMillis() -
		// start) + " ms");
		Map map = getData();
		System.out.println("string转字节长度:" + JSON.toJSONString(map).getBytes().length);
		System.out.println("JSON转字节长度:" + JSON.toJSONBytes(map).length);
		System.out.println("kryo转字节长度:" + setSerializableObject(map).length);

	}

	public static Map getData() {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < 200; i++) {
			map.put("aa" + i, i * i);
		}
		return map;
	}

	public static byte[] setSerializableObject(Object data) throws Exception {
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		// kryo.register(SimpleModel.class);
		kryo.register(data.getClass());
		// Output output = new Output(new FileOutputStream("D:/file1.bin"));
		// for (int i = 0; i < 100000; i++) {
		// Map<String, Integer> map = new HashMap<String, Integer>(2);
		// map.put("zhang0", i);
		// map.put("zhang1", i);
		// kryo.writeObject(output, new SimpleModel("zhang" + i, (i + 1), map));
		// }
		Output output = new Output(4096);
		kryo.writeObject(output, data);
		output.flush();
		output.close();
		// System.out.println("aaa:" + output.getBuffer().length);
		// System.out.println("bbb:" + output.toBytes().length);
		return output.toBytes();
	}

	public static void setSerializableObject() throws FileNotFoundException {
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		kryo.register(SimpleModel.class);
		Output output = new Output(new FileOutputStream("D:/file1.bin"));
		for (int i = 0; i < 100000; i++) {
			Map<String, Integer> map = new HashMap<String, Integer>(2);
			map.put("zhang0", i);
			map.put("zhang1", i);
			kryo.writeObject(output, new SimpleModel("zhang" + i, (i + 1), map));
		}
		// System.out.println("aaa:" + output.getBuffer().length);
		output.flush();
		output.close();
	}

	public static void getSerializableObject() {
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		Input input = null;
		try {
			input = new Input(new FileInputStream("D://file1.bin"));
			SimpleModel simple = null;
			SimpleModel simple1 = kryo.readObject(input, SimpleModel.class);
			System.out.println("ccc:" + simple1);
			// while ((simple = kryo.readObject(input, SimpleModel.class)) !=
			// null) {
			// System.out
			// .println("bbb:" + simple.getAge() + " " + simple.getName() + " "
			// + simple.getMap().toString());
			// }

			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (KryoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// input.close();
		}

	}

}
