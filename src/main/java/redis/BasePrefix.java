package redis;

public abstract class BasePrefix implements KeyPrefix {
	
	private String prefix;
	private int exprieSeconds;
	
	public BasePrefix(int exprieSeconds, String prefix) {
		
	}
	@Override
	public int expireSeconds() {
		// 默认0代表永不过期
		return 0;
	}

	@Override
	public String getPrefix() {
		String className = getClass().getSimpleName();
		return null;
	}
	

}
