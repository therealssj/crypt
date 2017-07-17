import base64
import hashlib
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP

KEY = "AAAAgMom/1a/v0lblO2Ubrt60J2gcuXSljGFQXgcyZWveWLEwo6prwgi3iJIZdodyhKZQrNWp5nKJ3srRXcUW+F1BD3baEVGcmEgqaLZUNBjm057pKRI16kB0YppeGx5qIQ5QjKzsR8ETQbKLNWgRY0QRNVz34kMJR3P/LgHax/6rmf5AAAAAwEAAQ=="


def readInt(bArr, i):
    return (((((bArr[i]&255) << 24)|0)|((bArr[i + 1]&255) << 16))|(
        (bArr[i + 2]&255) << 8))|(bArr[i + 3]&255)


def createKeyFromString(str, bArr):
    decode = base64.standard_b64decode(str)
    keyint = readInt(decode, 0)
    obj = decode[4:(4 + keyint)]
    bigInt = int.from_bytes(obj, byteorder='big')
    keyInt2 = readInt(decode, keyint + 4)
    obj2 = decode[keyint + 8:keyint + 8 + keyInt2]
    bigInt2 = int.from_bytes(obj2, byteorder='big')
    decode = hashlib.sha1(decode).digest()
    bArr[1:] = decode[:4]
    rsaKey = RSA.construct((bigInt, bigInt2))
    return rsaKey, bArr


def encryptString(inputstr):
    i = 0
    obj = bytearray(5)
    rsaKey, obj = createKeyFromString(KEY, obj)
    if rsaKey is None:
        return ""

    try:
        bytes = inputstr.encode('utf-8')
        bLen = int(((len(bytes) - 1) / 86)) + 1
        obj2 = bytearray(bLen * 133)
        encryptor = PKCS1_OAEP.new(rsaKey)

        while i < bLen:
            offset = i * 86
            strlen = (len(bytes) - offset if i == (bLen - 1) else 86) + offset

            encstr = encryptor.encrypt(bytes[offset:strlen])
            obj2[i*133:i*133+len(obj)] = obj
            obj2[i*133+len(obj):i*133+len(obj)+len(encstr)] = encstr
            i+=1

        print(base64.standard_b64encode(obj2))

    except Exception as e:
        print(e)
        return ""


encryptString("leemail" + '\u0000' + "lepassword")
