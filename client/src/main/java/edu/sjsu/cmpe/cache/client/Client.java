package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;

public class Client {
	private enum StringFunnel implements Funnel<String> {
		INSTANCE;

		public void funnel(String from, PrimitiveSink into) {
			into.putString(from);
		}

		@Override
		public String toString() {
			return "Funnels.stringFunnel()";
		}
	}

	public static void main(String[] args) throws Exception {
		// System.out.println("Starting Cache Client...");
		// CacheServiceInterface cache = new DistributedCacheService(
		// "http://localhost:3000");
		//
		// cache.put(1, "foo");
		// System.out.println("put(1 => foo)");
		//
		// String value = cache.get(1);
		// System.out.println("get(1) => " + value);
		//
		// System.out.println("Existing Cache Client...");
		//

		String[] servers = new String[3];
		servers[0] = "http://localhost:3000";
		servers[1] = "http://localhost:3001";
		servers[2] = "http://localhost:3002";

		String[] input = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j" };
		List<String> serverlist = new ArrayList<String>();
		Map<String, CacheServiceInterface> caches = new HashMap<String, CacheServiceInterface>();
		for (int i = 0; i < 3; i++) {
			serverlist.add(servers[i]);
			caches.put(servers[i], new DistributedCacheService(servers[i]));
		}

		// System.out.println("Starting Consistent Cache Client...");
		// ConsistentHash ch = new ConsistentHash(new MD5HashCode(), 100,
		// servers);
		// for (int i = 0; i < input.length; i++) {
		// String cachename = ch.get(i + 1);
		// caches.get(cachename).put(i + 1, input[i]);
		// System.out.println("put(" + (i + 1) + " => " + input[i] + ")");
		// }
		//
		// for (int i = 0; i < input.length; i++) {
		// String cachename = ch.get(i + 1);
		// String value = caches.get(cachename).get(i + 1);
		// System.out.println("get(" + (i + 1) + " => " + value + ")");
		// }
		//
		// System.out.println("Consistent Cache Client...");
		//

		RendezvousHash<Integer, String> rh = new RendezvousHash<Integer, String>(
				Hashing.murmur3_128(), Funnels.integerFunnel(), StringFunnel.INSTANCE,
				serverlist);
		System.out.println("Starting Rendezvous Cache Client...");
		for (int i = 0; i < input.length; i++) {
			String cachename = rh.get(i + 1);
			caches.get(cachename).put(i + 1, input[i]);
			System.out.println("put(" + (i + 1) + " => " + input[i] + ")");
		}

		for (int i = 0; i < input.length; i++) {
			String cachename = rh.get(i + 1);
			String value = caches.get(cachename).get(i + 1);
			System.out.println("get(" + (i + 1) + " => " + value + ")");
		}

		System.out.println("Rendezvous Cache Client...");
	}
}
