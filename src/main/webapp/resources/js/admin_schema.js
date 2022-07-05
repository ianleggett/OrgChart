	web_schema = {
			  type: "object",
			  title: " ",
			  properties: {					  
				  vendorType : {
			    	title: "Ecommerce site type",
			    	type: "string",
			    	format: "select",
					uniqueItems: true,
					enum: ["WooCommerce","Magento"],			    	
			    },			   
			    webUrl: {
			    	title: "Web url, base host name and URL",
			    	type: "string",
			    	default : "https://yoursite.com",
			    },			    
			    webUserName: {
			    	title: "Username",
			    	type: "string",
			    	default : "-not-set-",
			    },
			    
			    webPwd: {
			    	title: "password",
			    	type: "string",
			    	default : "password",
			    }
			  }
	};
	
	keys_schema = {
		type: "object",
			  title: " ",
			  properties: {
			    value: {
			    	title: "Private key",
			    	type: "string",
			    	default : "0x0000000",
			    }
 		}
	};
	
	crypto_schema ={			
			 type: "object",
			  title: " ",
			  properties: {
			    httpService: {
			    	title: "Url and port",
			    	type: "string",
			    	default : "http://",
			    },			    
			    escrowCtrAddr: {
			    	title: "Escrow Contract address on this network",
			    	type: "string",
			    	default : "-not-set-",
			    },			   
			    usdtcoinCtrAddr: {
			    	title: "USDT or Coin Address on this network",
			    	type: "string",
			    	default : "-not-set-",
			    },
			    sellerfeePct: {
			    	title: "Seller Fee for the transaction (%)",
			    	type: "number",
			    	default : 0.015,
					minimum : 0,
					maximum : 0.5					
			    },
			    buyerfeePct: {
			    	title: "Buyer Fee for the transaction (%)",
			    	type: "number",
			    	default : 0.015,
					minimum : 0,
					maximum : 0.5					
			    },	
 				sellerGasFee: {
			    	title: "Seller GAS Fee for the transaction (USDt)",
			    	type: "number",
			    	default : 25.0,
					minimum : 0,
					maximum : 100.0					
			    },
			    buyerGasFee: {
			    	title: "Buyer GAS Fee for the transaction (USDt)",
			    	type: "number",
			    	default : 25.0,
					minimum : 0,
					maximum : 100.0							
			    },	


				etherScanPrefix: {
			    	title: "Etherscan prefix URL include end backslash (e.g. https://kovan.etherscan.io/tx/ )",
			    	type: "string",
			    	default : "",
			    },
				maxEthFee: {
			    	title: "Max ETH per transaction",
			    	type: "number",
			    	default : 0.015,
					minimum : 0.00001,
					maximum : 0.1					
			    },
 				allowanceTimeout: {
			    	title: "time to wait for allowance to be set on chain (Seconds)",
			    	type: "integer",
			    	default : 40,
	    			minimum : 0,
	    			maximum : 360,	    			
			    },	
   			   transTimeout: {
			    	title: "Transaction Timeout for block transactions not returning a result yet (Seconds)",
			    	type: "integer",
			    	default : 300,
	    			minimum : 0,
	    			maximum : 50000,	    			
		    },	
	
			  }
	};
	
		email_schema = {
		  type: "object",
		  title: " ",
		  properties: {		
			simulateSending : {
			    title: "Dont send emails, simulate sending",
	            type: "boolean",
	            format: "checkbox",
	            propertyOrder : 1
	        },	            
		    emailCheckMins: {
		    	title: "Number of minutes to check and send emails (0=disable sending emails)",
		    	type: "integer",
		    	default : 10,
    			minimum : 0,
    			maximum : 1440,
    			propertyOrder: 10
		    },			
	          emailOnUserRegistration : {
			    	title: "Email new user registration",
	                type: "boolean",
	                format: "checkbox",
	                propertyOrder : 20
	            },	            
	          emailOnNewOrder : {
			    	title: "Email when a new order is created",
	                type: "boolean",
	                format: "checkbox",
	                propertyOrder : 21
	            },	            
	          emailOnDeposit : {
			    	title: "Email when counter party leave deposit",
	                type: "boolean",
	                format: "checkbox",
	                propertyOrder : 22
	            },	
	          emailOnCancel : {
			    	title: "Email when a cancel is issued",
	                type: "boolean",
	                format: "checkbox",
	                propertyOrder : 23
	            },	
	          emailOnBankDeposit : {
			    	title: "Email when bank deposit flagged",
	                type: "boolean",
	                format: "checkbox",
	                propertyOrder : 24
	            },	
	          emailOnBankRec : {
			    	title: "Email  when bank deposit received",
	                type: "boolean",
	                format: "checkbox",
	                propertyOrder : 25
	            },	
	          emailOnComplete : {
			    	title: "Email when order is complete",
	                type: "boolean",
	                format: "checkbox",
	                propertyOrder : 26
	            },	
          host: {
                type: "string",
                minLength: 1
              },
              port: {
                type: "integer",
                minimum : 10,
                maximum : 65535
              },	              
              username: {
                type: "string",
                minLength: 1
              },	              
              password: {
                  type: "string",
                  minLength: 1,
                  format: "password"
              },              
              mail_transport_protocol : {
                type: "string",
                enum: [
                    "smtp",
                    "none"
                  ]
              },
              auth : {
                type: "boolean",
                format: "checkbox"
              },
              starttls: {
                type: "boolean",
                format: "checkbox"	              
              },
			fromEmail: {
		    	title: "from / reply to email",
                type: "string",
		    	default : "noreply@tinance.io",
                minLength: 1				
			},
			staffList: {
		    	title: "list of staff emails (comma separated)",
                type: "string",
		    	default : "",
                minLength: 0				
			},
			notifyStaff : {
			   	title: "Notify staff emails",
	            type: "boolean",
	            format: "checkbox",
	            propertyOrder : 30
	        },			
		    emailValidation: {
		    	title: "Regex format for email e.g ^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$",
                type: "string",
		    	default : "^\\\\d{10,15}$",
                minLength: 1
             }
     	}
};

	trade_schema = {
		  type: "object",
		  title: " ",
		  properties: {			    
			    minsAcceptExpire: {
					    	title: "Minimum expiry time after offer is taken (mins)",
					    	type: "number",
					    	default : 60,
				    		minimum : 10,
				    		maximum : 480,	
				    		propertyOrder: 1
					    },
	           minsDepositExpire: {
					    	title: "Minimum time after a deposit is made (mins)",
					    	type: "number",
					    	default : 0,
				    		minimum : 10,
				    		maximum : 480,	
				    		propertyOrder: 2
					    },
  			},		 
}



staff_schema = {
	  	type : "array",
	  	title: " ",
	  	format: "table",
	      items : {
	      	type:"object",
	      	properties: {
	              id: {
		               type: "string",
		               format:"hidden", 
		               default: null
		              },		              	      		
	              username: {
	                type: "string",
	                minLength: 1
	              },		              
	              password: {
	                  type: "string",
	                  minLength: 1,
	                  format: "password"
	              }, 
	              email: {
		                type: "string",
		                minLength: 1
		          },
	              role : {
	            	  type: "string",
	            	  enum:["ROLE_GUEST",
	            		"ROLE_USER",	            		
	            		"ROLE_ADMIN",
	            		"ROLE_SUPER"]
	              },
	              verified : {
	                  type: "boolean",
	                  format: "checkbox"
	              },				  
	              enabled : {
	                  type: "boolean",
	                  format: "checkbox"
	              }
	              
	      	}
	      }
}
	venue_schema = {
			type : "array",
		  	title: " ",
		  	format: "table",
		      items : {
		      	type:"object",
		      	properties: {
			      		 id: {
				               type: "string",
				               format:"hidden", 				               
				               propertyOrder: 1
				              },
				         enabled : {
					           type: "boolean",
					           format: "checkbox",
					           propertyOrder: 2
					        },
					    venueName: {
					    	title: "Venue Name",
					    	type: "string",
					    	default : "-name-",
					    	propertyOrder: 3
					    },	
					    limitOrMarket:{
					    	title: "order type",
					    	type: "string",					    	
			            	enum:["limit",
			            		"market"],
			            	propertyOrder: 4
					    },					    
					    makerFee: {
					    	title: "Maker Fee (%)",
					    	type: "number",
					    	default : 0.0,
				    		minimum : 0,
				    		maximum : 50,	
				    		propertyOrder: 5
					    },
					    takerFee: {
					    	title: "Taker Fee (%)",
					    	type: "number",
					    	default : 0.0,
				    		minimum : 0,
				    		maximum : 50,	
				    		propertyOrder: 6
					    },					   
					    apiKey: {
					    	title: "API Key",
					    	type: "string",			    	
					    },
					    apiSecret: {
					    	title: "API secret",
					    	type: "string",	
					    	minLength: 1,
			                format: "password"
					    }, demo: {
					    	title: "demo-token (blank=production)",
					    	type: "string",
					    	default:""
					    },
			  }
		      }
	};
	
sym_schema = {
		  	type : "array",
		  	title: " ",
		  	format: "table",
		      items : {
		      	type:"object",
		      	properties: {
		      		symPair: {
		                type: "string",
		                minLength: 1
		              },		              		            
		              enable : {
		                  type: "boolean",
		                  format: "checkbox"
		              },
		              baseCcy: {
			                type: "string",
			                minLength: 1,
			                readonly: true
			              },
			          quoteCcy: {
				            type: "string",
				            minLength: 1,
				            readonly: true
				      }		              
		      	}
		      }
	}	