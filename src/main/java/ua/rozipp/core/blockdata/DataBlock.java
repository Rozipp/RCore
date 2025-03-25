package ua.rozipp.core.blockdata;

import org.bukkit.block.Block;
import redempt.redlib.json.JSONList;
import redempt.redlib.json.JSONMap;

import java.util.Map;
import java.util.Set;

/**
 * Represents a Block with data attached to it
 *
 * @author Redempt
 */
public class DataBlock {

	protected JSONMap data;
	private final BlockDataManager manager;
	private final BlockPosition blockPosition;
	private boolean delete = false;

	DataBlock(JSONMap data, BlockPosition blockPosition, BlockDataManager manager) {
		this.data = data;
		this.blockPosition = blockPosition;
		this.manager = manager;
	}

	/**
	 * @return The BlockDataManager this DataBlock belongs to
	 */
	public BlockDataManager getManager() {
		return manager;
	}

	/**
	 * @return The Block the data is attached to
	 */
	public Block getBlock() {
		return blockPosition.getBlock();
	}

	public ChunkPosition getChunkPosition() {
		return blockPosition.getChunkPosition();
	}

	public BlockPosition getBlockPosition() {
		return blockPosition;
	}

	/**
	 * Gets an object by key
	 *
	 * @param key The key the data is mapped to
	 * @return The data as an Object
	 */
	public Object getObject(String key) {
		return data.get(key);
	}
	
	/**
	 * Gets a string by key
	 * @param key The key the data is mapped to
	 * @return The data as a String
	 */
	public String getString(String key) {
		return data.getString(key);
	}
	
	/**
	 * Gets an int by key
	 * @param key The key the data is mapped to
	 * @return The data as an Integer
	 */
	public Integer getInt(String key) {
		return data.getInt(key);
	}
	
	/**
	 * Gets a long by key
	 * @param key The key the data is mapped to
	 * @return The data as a Long
	 */
	public Long getLong(String key) {
		return data.getLong(key);
	}
	
	/**
	 * Gets a Double by key
	 * @param key The key the data is mapped to
	 * @return The data as a Double
	 */
	public Double getDouble(String key) {
		return data.getDouble(key);
	}
	
	/**
	 * Gets a Boolean by key
	 * @param key The key the data is mapped to
	 * @return The data as a Boolean
	 */
	public Boolean getBoolean(String key) {
		return data.getBoolean(key);
	}
	
	/**
	 * Gets a JSONList by key
	 * @param key The key the data is mapped to
	 * @return The data as a JSONList
	 */
	public JSONList getList(String key) {
		return data.getList(key);
	}
	
	/**
	 * Gets a JSONMap by key
	 * @param key The key the data is mapped to
	 * @return The data as a JSONMap
	 */
	public JSONMap getMap(String key) {
		return data.getMap(key);
	}
	
	/**
	 * Checks if a certain key is used in this DataBlock
	 * @param key The key
	 * @return Whether the key is used
	 */
	public boolean contains(String key) {
		return data.containsKey(key);
	}
	
	/**
	 * Clears all data from this DataBlock
	 */
	public void clear() {
		data.clear();
	}
	
	/**
	 * Sets data in this DataBlock
	 * @param key The key to set the data with
	 * @param value The data
	 */
	public void set(String key, Object value) {
		manager.setModified(blockPosition.getChunkPosition());
		if (value == null)
			data.remove(key);
		else
			data.put(key, value);
	}
	
	/**
	 * Removes a key from this DataBlock
	 * @param key The key to remove
	 */
	public void remove(String key) {
		set(key, null);
	}

	/**
	 * @return All data stored in this DataBlock
	 */
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * @return All keys used in this DataBlock
	 */
	public Set<String> getKeys() {
		return data.keySet();
	}

	public void remove() {
		this.delete = true;
	}

	public void setData(JSONMap data) {
		this.data = data;
	}

	public boolean isDelete() {
		return delete;
	}

	@Override
	public String toString() {
		return blockPosition.toString() + "; " + data;
	}
}
