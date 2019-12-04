export interface RNAccountManagerAccount {
  _index: number
  name: string
  type: string
}

export interface RNAccountManager {
  getAccountsByType(accountType: string): Promise<RNAccountManagerAccount[]>
  addAccountExplicitly(accountType: string, userName: string, password: string): Promise<RNAccountManagerAccount>
  removeAccount(accountObject: RNAccountManagerAccount): Promise<{}>
  setUserData(accountObject: RNAccountManagerAccount, key: string, data: string): Promise<{}>
  getUserData(accountObject: RNAccountManagerAccount, key: string): Promise<{ value: string }>
}

declare const manager: RNAccountManager

export default manager
