package com.github.pierrebeucher.quark.cmis.context;

public enum CMISService {
	ACL("ACLService"),
	DISCOVERY("DiscoveryService"),
	MULTIFILING("MultiFilingService"),
	NAVIGATION("NavigationService"),
	OBJECT("ObjectService"),
	POLICY("PolicyService"),
	RELATIONSHIP("RelationshipService"),
	REPOSITORY("RepositoryService"),
	VERSIONING("VersioningService");

	private String service;

	CMISService(String service){
		this.service = service;
	}

	public String value(){
		return this.service;
	}
}
