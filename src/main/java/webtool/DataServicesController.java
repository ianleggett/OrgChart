package webtool;
//package webtool;
//
//import java.util.List;
//import java.util.Map;
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import webtool.pojo.ContractMatch;
//import webtool.pojo.CustomerOrder;
//import webtool.pojo.WebOfferObject;
//import webtool.pojo.RespStatus;
//import webtool.pojo.UserAndRole;
//import webtool.pojo.UserCoin;
//import webtool.pojo.WebAgreeMatch;
//import webtool.pojo.WebFilterObject;
//import webtool.pojo.WebUserCoin;
//import webtool.service.ShimDataServices;
//import webtool.utils.TestCtrCreateObject;
//
//@Secured({ "ROLE_USER","ROLE_ADMIN" })
//@RestController
//public class DataServicesController {
//	static Logger log = Logger.getLogger(DataServicesController.class);
//
//	private ObjectMapper objectMapper = new ObjectMapper();
//	
////	@RequestMapping(value = "/addpaymethod.json", method = RequestMethod.POST, consumes = {
////	MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
////@ResponseBody
////public RespStatus addpaymethod(@RequestBody WebAddPaymentDetail postData) {
////log.info("addpaymethod : "+postData);	
////Optional<UserAndRole> optusr = securityService.getCurrentUser();
////if (optusr.isPresent()) {
////		Optional<PaymentType> payType = paymentTypeRepository.findById( postData.getPayid() );
////		if (payType.isPresent()) {
////			log.info("addpaymethod : "+payType);
////			PaymentDetail payDetail = new PaymentDetail(payType.get(), postData.getPayDetails());
////			userPaymentRepository.save(payDetail);
////			optusr.get().getPaymentMethods().add( payDetail );
////			
////			userRepository.save(optusr.get());
////			log.info("saved : "+optusr.get());	
////			return RespStatus.OK;
////		}
////		return new RespStatus(99,"Bad Pay Type");
////}
////return new RespStatus(99,"User mismatch");
////}
//	
////	@RequestMapping(value = "/getCustomerTokenInfo.json", method = RequestMethod.GET)
////	public List<WebTokenInfo> getToken() throws Exception {
////		List<WebTokenInfo> tokList = new ArrayList<WebTokenInfo>();
////		Optional<UserAndRole> optusr = securityService.getCurrentUser();
////		if (optusr.isPresent()) {
////			// for this user, grab all tokens and balances
////			UserAndRole uar = optusr.get();
////			uar.getMycoins().forEach( ucoin ->{
////				Ccy ccy = ucoin.getCoinType();
////				if (ccy.getCcyType()==CcyType.ERC20) {
////					tokList.add( new WebTokenInfo(
////						ucoin.getId(),
////						//cryptoService.getInternalBalance(ccy.getTokenCtrAddress(),ucoin.getWalletAddress()).intValue()
////						Double.NaN
////					));
////				}
////			});			
////		}
////		return tokList;
////	}
//	
//
////	@RequestMapping(value = "/deleteusercoin.json", method = RequestMethod.POST, consumes = {
////			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
////	@ResponseBody
////	public RespStatus addupdatecoin( @RequestParam(value = "cid", required = true) long cid ) {
////		log.info("deleteusercoin "+cid);
////		Optional<UserAndRole> optusr = securityService.getCurrentUser();
////		if (optusr.isPresent()) {	
////			
////			if (optusr.get().deleteMyCoin(cid)) {				
////				userRepository.save( optusr.get() );
////				userCoinRespository.deleteById(cid);			
////				return RespStatus.OK;			
////			}			
////		}	
////		return new RespStatus(99,"Could not delete user coin "+cid);
////	}
//	
////	@RequestMapping(value = "/addupdatecoin.json", method = RequestMethod.POST, consumes = {
////			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
////	@ResponseBody
////	public RespStatus addupdatecoin(@RequestBody WebUserCoin postData) {
////		log.info("addupdatecoin "+postData.toString());
////		Optional<UserAndRole> optusr = securityService.getCurrentUser();
////		if (optusr.isPresent()) {
////			Optional<Ccy> ccyopt = fxcodeRepository.findById(postData.getCoinid());
////			if (ccyopt.isPresent()) {				
////				UserCoin uc = new UserCoin(ccyopt.get(),postData.getWalletAddr());
////				uc.setId(postData.getCoinid());			
////				uc = userCoinRespository.save(uc);
////				optusr.get().getMycoins().add( uc );
////				userRepository.save( optusr.get() );
////				return RespStatus.OK;
////			}			
////		}	
////		return new RespStatus(99,"Could not find user or CCY "+postData.getCoinid());
////	}
//		
//	@Autowired
//	ShimDataServices shimDataServices;
//		
//	@RequestMapping(value = "/debugDeleteTrades.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
//	@ResponseBody
//	public RespStatus debugDeleteTrades() {
//		return shimDataServices.debugDeleteTrades();		
//	}
//		
////	@RequestMapping(value = "/createcontract.json", method = RequestMethod.POST, consumes = {
////			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
////	@ResponseBody
////	public RespStatus createcontract(@RequestBody TestCtrCreateObject postData) {
////		return shimDataServices.createcontract(postData);
////	}
//	
//	@RequestMapping(value = "/depositcrypto.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE }) 
//	@ResponseBody
//	public RespStatus depositCrypto(@RequestParam(value = "oid", required = true) String oid) {
//		return shimDataServices.depositCrypto(oid);
//	}
//	
//	@RequestMapping(value = "/flagfundssent.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE }) 
//	@ResponseBody
//	public RespStatus flagbankfundssent(@RequestParam(value = "oid", required = true) String oid) {
//		return shimDataServices.flagbankfundssent(oid);		
//	}
//	
//	@RequestMapping(value = "/flagcomplete.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE }) 
//	@ResponseBody
//	public RespStatus flagcomplete(@RequestParam(value = "oid", required = true) String oid,@RequestParam(value = "txn", required = true) String txn) {
//		return shimDataServices.flagcomplete(oid,txn);
//	}
//		
//	@RequestMapping(value = "/getwallets.json", method = RequestMethod.GET)
//	public Map<String,UserCoin> getwallets() {
//		return shimDataServices.getwallets();
//	}	
//	
//	@RequestMapping(value = "/toggleLive.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
//	@ResponseBody
//	public RespStatus toggleLive(@RequestParam(value = "kid", required = true) long kid, @RequestParam(value = "v", required = true) boolean v) {
//		return shimDataServices.toggleLive(kid,v);		
//	}
//	
//	@RequestMapping(value = "/deleteoffer.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
//	@ResponseBody
//	public RespStatus deleteoffer(@RequestParam(value = "kid", required = true) long kid) {
//		return shimDataServices.deleteoffer(kid);
//	}
//	
//	@RequestMapping(value = "/extendoffer.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
//	@ResponseBody
//	public RespStatus extendoffer(@RequestParam(value = "kid", required = true) long kid) {
//		return shimDataServices.extendoffer(kid);
//	}
//	
//	@RequestMapping(value = "/getoffer.json", method = RequestMethod.GET)
//	@ResponseBody
//	public CustomerOrder getOffer( @RequestParam(value = "oid", required = true) String orderid) {		
//		return shimDataServices.getOffer(orderid);	
//	}	
////	/**
////	 * Get Market Orders
////	 * @param request
////	 * @param response
////	 * @return
////	 */   /// Browse Offers 
////	@RequestMapping(value = "/useroffers.json", method = RequestMethod.GET)
////	@ResponseBody
////	public List<CustomerOrder> useroffers(@RequestBody WebFilterObject filter) {
////		return shimDataServices.useroffers(filter);
////	}
//	/**
//	 * Get the user trading orders & history
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/mytrades.json", method = RequestMethod.GET)
//	@ResponseBody
//	public List<ContractMatch> mytrades(@RequestBody WebFilterObject filter) {
//		return shimDataServices.mytrades(filter);
//	}	
//	
//	@RequestMapping(value = "/getuserdetails.json", method = RequestMethod.GET)
//	public UserAndRole getuserdetails() {
//		return shimDataServices.getuserdetails();
//	}
//	
//	@RequestMapping(value = "/getCoin.json", method = RequestMethod.GET)
//	public UserCoin getCoin(@RequestParam(value = "cid", required = true) long coinId) {
//		return shimDataServices.getCoin(coinId);
//	}
//	
//	@RequestMapping(value = "/getUserCoins.json", method = RequestMethod.GET)
//	public List<UserCoin> getUserCoins() {
//		return shimDataServices.getUserCoins();
//	}
//	/**
//	 * Set USDT coin during wallet connect
//	 * @param postData
//	 * @return
//	 */
//	@RequestMapping(value = "/setusercoin.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
//	@ResponseBody
//	public RespStatus setusercoin(@RequestBody WebUserCoin postData) {
//		return shimDataServices.setusercoin(postData);	
//	}
//	
//	@RequestMapping(value = "/addupdateorder.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
//	@ResponseBody
//	public RespStatus addupdateorder(@RequestBody WebOfferObject postData) {
//		return shimDataServices.addupdateorder(postData);		
//	}
//	
//	@RequestMapping(value = "/takeorder.json", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
//	@ResponseBody
//	public RespStatus takeorder(@RequestBody WebAgreeMatch postData) {
//		return shimDataServices.takeorder(postData);
//	}
//	
//	
//}
