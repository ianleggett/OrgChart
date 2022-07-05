package webtool.utils;

public class CryptNetworkConfig {
	
	String httpService = "http://";  // "http localhost or Mainnet address
	
	String escrowCtrAddr = "";  // Contract Address Escrow
	
	String USDTCoinCtrAddr = ""; // whatever the version of Coin Address on this network
	
	String etherScanPrefix = ""; // prefix for lookup 
	
	double sellerfeePct = 0.015;
	double buyerfeePct = 0.015;
	
	double sellerGasFee = 49.0; // default
	double buyerGasFee = 51.0; // default
	
	double maxEthFee = 0.001; // 3 usd	
	
	int allowanceTimeout = 40;  // time to wait for allowance to be set
	//int createCtrTime = 5;  // time to settle allowance to blockchain, before contract is invoked
	long transTimeout = 600;
	
	public CryptNetworkConfig() {
		super();
	}

//	public CryptNetworkConfig(String httpService, String escrowCtrAddr,
//			String uSDTCoinCtrAddr,String etherScanPrefix) {
//		super();
//		this.httpService = httpService;
//		this.escrowCtrAddr = escrowCtrAddr;
//		this.USDTCoinCtrAddr = uSDTCoinCtrAddr;
//		this.etherScanPrefix = etherScanPrefix;
//	}

	public String getHttpService() {
		return httpService;
	}

	public void setHttpService(String httpService) {
		this.httpService = httpService;
	}

	public String getEscrowCtrAddr() {
		return escrowCtrAddr;
	}

	public void setEscrowCtrAddr(String escrowCtrAddr) {
		this.escrowCtrAddr = escrowCtrAddr;
	}

	public String getUSDTCoinCtrAddr() {
		return USDTCoinCtrAddr;
	}

	public void setUSDTCoinCtrAddr(String uSDTCoinCtrAddr) {
		USDTCoinCtrAddr = uSDTCoinCtrAddr;
	}

	public double getSellerfeePct() {
		return sellerfeePct;
	}

	public void setSellerfeePct(double sellerfeePct) {
		this.sellerfeePct = sellerfeePct;
	}

	public double getBuyerfeePct() {
		return buyerfeePct;
	}

	public void setBuyerfeePct(double buyerfeePct) {
		this.buyerfeePct = buyerfeePct;
	}

	public String getEtherScanPrefix() {
		return etherScanPrefix;
	}

	public void setEtherScanPrefix(String entherScanPrefix) {
		this.etherScanPrefix = entherScanPrefix;
	}

	public double getMaxEthFee() {
		return maxEthFee;
	}

	public void setMaxEthFee(double maxEthFee) {
		this.maxEthFee = maxEthFee;
	}

	public int getAllowanceTimeout() {
		return allowanceTimeout;
	}

	public void setAllowanceTimeout(int allowanceTimeout) {
		this.allowanceTimeout = allowanceTimeout;
	}

	public double getSellerGasFee() {
		return sellerGasFee;
	}

	public void setSellerGasFee(double sellerGasFee) {
		this.sellerGasFee = sellerGasFee;
	}

	public double getBuyerGasFee() {
		return buyerGasFee;
	}

	public void setBuyerGasFee(double buyerGasFee) {
		this.buyerGasFee = buyerGasFee;
	}

	public long getTransTimeout() {
		return transTimeout;
	}

	public void setTransTimeout(long transTimeout) {
		this.transTimeout = transTimeout;
	}

	
	
}
