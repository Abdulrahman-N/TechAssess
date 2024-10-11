# Tech Assessment


<h2>Justification for Using PKCS7Padding Instead of PKCS5Padding:</h2>

The assessment requested PKCS5Padding, but after research, I found that PKCS5Padding is specifically designed for 8-byte block ciphers, such as DES.
Since AES uses 16-byte blocks, PKCS7Padding is the appropriate choice as it is an extension of PKCS5Padding, designed to work with any block size, including AES's 16-byte blocks.

In modern cryptography, PKCS7Padding is widely adopted for AES because it provides the same padding mechanism as PKCS5 but is compatible with larger block sizes, ensuring correct encryption and decryption with AES.

<h2>Project Architecture</h2>

* Clean architicture with MVVM as a prestnation design pattern 
* The project is built with single activiy as a design choice 
* Dagger hilt as a Dependency Injection framework
* RxJava for threading
* Navigation Component for navigation

<h2>Screenshots</h2>

<img src="https://i.imgur.com/GenpNFi.png" width="300">
<img src="https://i.imgur.com/4wv8h8l.png" width="300">
<img src="https://i.imgur.com/ZhGVIPq.png" width="300">
