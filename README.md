Microserice to create account and transfer money from one account to other.

Following is the implementation details

1.By invoking v1/accounts Create account with proper id and balance

2. By invoking v1/accounts/transfer initiate money transfer
    Monery transfer service will perform following checks
    1. Validate account ids are valid and present else return error response
    2. Validate account has sufficient balance to perform required transaction
    3. Perform transaction synchronously using synchronized methods for therad safety
  
To make this code production ready it need to connect database to store the account details in database.
To handle data consistency in database besides synchronized methods we can make use of @Transacinal annotation 
feature to mark data base transaction and we can perform operation in isolation to avoid data inconsistency.

Create account service
v1/accounts
{
    "accountId": "456",
    "balance": 5000
}

Money Transfer service
 v1/accounts/transfer
    {
    "accountFromId": "789",
    "accountToId": "456",
    "transferAmount": 12000
}

Get account details for account id
v1/accounts/{accountId}

