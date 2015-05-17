package edu.sjsu.cmpe.cache.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import edu.sjsu.cmpe.cache.domain.Entry;

public class ChronicleMapCache implements CacheInterface {
	ChronicleMap<Long, Entry> chronMap;

	public ChronicleMapCache(String suff) throws IOException {
		chronMap = ChronicleMapBuilder.of(Long.class, Entry.class)
				.createPersistedTo(new File("/tmp/chronicle.txt" + suff));
	}

	@Override
	public Entry save(Entry newEntry) {
		checkNotNull(newEntry, "newEntry instance must not be null");
		chronMap.putIfAbsent(newEntry.getKey(), newEntry);

		return newEntry;
	}

	@Override
	public Entry get(Long key) {
		checkArgument(key > 0,
				"Key was %s but expected greater than zero value", key);
		return chronMap.get(key);
	}

	@Override
	public List<Entry> getAll() {
		return new ArrayList<Entry>(chronMap.values());
	}

}
