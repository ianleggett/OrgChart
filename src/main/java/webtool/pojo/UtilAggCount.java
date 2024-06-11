package webtool.pojo;

public class UtilAggCount{
	
	Long cid;
	Long cnt;

	public UtilAggCount(Long cid, Long cnt) {
		super();
		this.cid = cid;
		this.cnt = cnt;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getCnt() {
		return cnt;
	}
	public void setCnt(Long cnt) {
		this.cnt = cnt;
	}
	@Override
	public String toString() {
		return "UtilAggCount [cid=" + cid + ", cnt=" + cnt + "]";
	}
	
	
}