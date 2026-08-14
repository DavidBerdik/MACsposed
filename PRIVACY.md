# Privacy Policy for MACsposed

**Effective Date:** August 14, 2026

## 1. Introduction
This Privacy Policy applies to the **MACsposed** Android application (the "App"), package name `com.berdik.macsposed`. MACsposed is a closed-source, proprietary Xposed module designed to block MAC address randomization on Android 12 through 17 and to provide a user-friendly interface for spoofing your WiFi MAC address. The App requires a rooted device, the LSPosed framework (API 101), and a valid in-app purchase — either a subscription or a lifetime license — to unlock its functionality.

We are committed to protecting your privacy and being transparent about our practices.

## 2. Data Collection and Use
**MACsposed does not collect, store on developer-operated servers, transmit to the developer, or share with the developer any personal information or user data.**

The App operates locally on your device. We do not use analytics, tracking frameworks, background telemetry, or data collection software development kits (SDKs). No information regarding your device hardware, your WiFi configuration, the MAC addresses you choose to use, your license status, or your personal identity is ever transmitted to the developer.

The App does store certain configuration data **locally on your device** so that it can function. This includes, for example, your configured WiFi MAC addresses, module settings, and a local licensed-or-unlicensed state derived from Google Play. This information remains on your device and is not sent to the developer.

Optional debug logging, when enabled in the App's settings, writes hook-related messages to the local LSPosed log on your device. These logs are not transmitted to the developer. Certain error messages related to module initialization may also be written to the local LSPosed log regardless of the logging setting.

### In-App Purchases and License Validation
Purchases are completed through Google Play. The App queries Google Play to verify whether you hold a valid subscription or lifetime license and stores only a local licensed-or-unlicensed state on your device. Purchase tokens and receipts are not stored by the App or transmitted to the developer; they are handled by Google Play as part of the purchase and acknowledgment process.

Uninstalling the App removes locally stored MACsposed data from the App's storage. Google Play retains purchase records in accordance with Google's policies.

## 3. Required Permissions and Device Access
To function correctly, MACsposed requires root access and the LSPosed framework. These elevated privileges are used strictly locally to hook into system WiFi and MAC randomization APIs, to apply your chosen MAC address settings, and to integrate with Android Settings and System UI.

The App also uses component-level permissions for internal coordination, including `LOCAL_MAC_ADDRESS` and permissions required for its Quick Settings tile.

* **Local Processing Only:** All system modifications, MAC address changes, and module operations are performed locally on your device. The App may also write supporting files under `/data/adb/` on rooted devices as part of normal installation and operation.
* **No Remote Access:** The developer has no access to your device, your MAC address settings, your WiFi state, or any actions you perform in the App.

## 4. Third-Party Services
MACsposed does not integrate with advertising networks, analytics providers, or developer-operated servers that receive user data. The App does integrate with the following third-party services for licensing purposes only:

### Google Play and Google Play Billing
MACsposed uses the Google Play Billing Library to sell and validate subscriptions and lifetime licenses. When you purchase or manage a license, **Google** processes the transaction. Data Google may process includes your Google account information and payment details, and is governed by [Google's Privacy Policy](https://policies.google.com/privacy).

The App communicates with Google Play services to display purchase flows, query your entitlements, acknowledge purchases, and show transactional messages from Google Play (such as payment recovery or subscription price-change notices). The developer does not receive your payment card numbers, billing address, or other payment credentials.

Because the developer does not collect user data, no user data is shared with, sold to, or made accessible to the developer by third parties. Data processed by Google Play is handled solely under Google's policies.

## 5. Proprietary Software
MACsposed is closed-source, proprietary commercial software. The source code is not publicly available and cannot be independently audited.

The public GitHub repository at [github.com/DavidBerdik/MACsposed](https://github.com/DavidBerdik/MACsposed) is provided for issue reporting and product information only. It does not distribute source code. The developer commits to the data-handling practices described in this Privacy Policy.

## 6. Children's Privacy
MACsposed does not knowingly collect any personally identifiable information from children under the age of 13. Given the technical nature of the App — which requires a rooted device, the LSPosed framework, and a paid license — it is not directed at or intended for use by children. If you believe a child has provided us with personal information, please contact us so it can be addressed.

## 7. Changes to This Privacy Policy
We may update our Privacy Policy from time to time. Since the developer does not collect user data, any changes will likely reflect updates to App functionality, licensing, or new Google Play regulatory requirements. We will notify you of any changes by updating the "Effective Date" at the top of this policy and publishing the revised policy on the official GitHub repository at [github.com/DavidBerdik/MACsposed](https://github.com/DavidBerdik/MACsposed).

## 8. Contact Us
If you have any questions, concerns, or suggestions regarding this Privacy Policy, please contact the developer by opening an issue on the official GitHub repository:
https://github.com/DavidBerdik/MACsposed/issues
