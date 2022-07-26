package webtool.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "oview")
public class OrgView {

    @Id	
	String name;   // default org view is read only
	String descr;
	Boolean readOnly;
	
//	@OneToMany(targetEntity=OrgContainer.class, fetch=FetchType.EAGER,cascade = CascadeType.ALL)	
//	List<OrgContainer> containers = new ArrayList<OrgContainer>();
//	@OneToMany(targetEntity=OrgLink.class, fetch=FetchType.LAZY,cascade = CascadeType.ALL)
//	List<OrgLink> links = new ArrayList<OrgLink>();
//	
//	/******* fast access, not persisted *********/
//	@Transient
//	Map<String,OrgLink> linkMap = new HashMap<String,OrgLink>();
//	/******* fast access, not persisted *********/
//	@Transient
//	Map<String,OrgContainer> containerMap = new HashMap<String,OrgContainer>();
//	
//	public void add(OrgContainer cont) {
//		containers.add(cont);
//		containerMap.put(cont.getFQDN(),cont);
//	}
//	public void add(OrgLink link) {
//		links.add(link);
//		linkMap.put(link.getIdHash(),link);
//	}
//	
//	public Map<String,OrgLink> getLinkMap(){
//		if (linkMap==null) {
//			linkMap = links.stream().collect(Collectors.toMap( OrgLink::getIdHash, it->it) );
//		}
//		return linkMap;
//	}
//	
//	public Map<String,OrgContainer> getContainerMap(){
//		if (containerMap==null) {
//			containerMap = containers.stream().collect(Collectors.toMap( OrgContainer::getFQDN, it->it) );
//		}
//		return containerMap;
//	}
	
	public OrgView() {
		super();
	}
	public OrgView(String name, String descr, Boolean readOnly) {
		super();
		this.name = name;
		this.descr = descr;
		this.readOnly = readOnly;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
//	public List<OrgContainer> getContainers() {
//		return containers;
//	}
//	public void setContainers(List<OrgContainer> containers) {
//		this.containers = containers;
//	}
//	public List<OrgLink> getLinks() {
//		return links;
//	}
//	public void setLinks(List<OrgLink> links) {
//		this.links = links;
//	}
	
}
