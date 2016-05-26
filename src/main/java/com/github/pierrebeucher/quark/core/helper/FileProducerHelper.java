package com.github.pierrebeucher.quark.core.helper;

import com.github.pierrebeucher.quark.core.context.base.HelperContext;

/**
 * <p>This Helper represent a service producing data as files. Produced data may be of any kind
 * depending on this Helper implementation, such as local file system creation, file upload on a server,
 * or any other file flavour.</p>
 * <p>Basic implementation for a SFTP service, where a File is produced on a server:
 * <pre>
 * class SomeCompanyServiceHelper extends AbstractHelper&lt;SomeCompanyServiceContext&gt; implements FileProducerHelper{
 * 
 * 	public SomeCompanyServiceHelper(SomeCompanyServiceContext context){
 * 		super(context);
 * 	}
 * 
 * 	public void producataData(){
 * 		SftpServiceHelper producer = new SftpServiceHelper(sftpContext);
 * 		producer.produceFile(new FileInputStream(context.getFile(), context.getDestination(), context.getFile().getName());
 * 	}
 * 
 * 	public HelperResult checkServiceProcessing(){
 * 		//typically, these methods will perform check to ensure the service processed data properly
 * 	}
 * }
 * </pre> 
 * @author pierreb
 *
 */
public interface FileProducerHelper extends Helper {
	
	/**
	 * Produce data for the service managed by this helper. 
	 */
	public void produceData() throws HelperException;
	
	/**
	 * The produce filename is the filename as it is produced by this Helper.
	 * Each FileProducerHelper produce one and only one filename, which can be retrieved 
	 * using the method.
	 * @return this Helper's produce filename
	 */
	public String getProduceFilename();
	
	/**
	 * Check whether the data produced by this Helper has been consumed.
	 * This function assumes a previous call to {@link #produceData()} 
	 * has been done. 
	 * @return true if data is consumed, false otherwise
	 */
	public boolean isDataConsumed();
	
	/**
	 * The produce context is the context under which the data is produced.
	 * @return the produce context
	 */
	public HelperContext getProduceContext();

}
