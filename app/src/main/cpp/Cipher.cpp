//
// Created by susgup on 6/11/2019.
//

#include <string.h>
#include <stdlib.h>
#include <set>
#include <iostream>
#include <vector>
#include "com_sonudoo_AccountKeeper_Cipher.h"

void
generateMatrix(int passcode, std::vector<int> &encryptMatrix, std::vector<int> &decryptMatrix) {
    /*
     * The following code generates (1<<6) unique random integers and then places other integers.
     * The algorithm is a modification of polyalphabetic cipher.
     */
    srand(passcode);
    std::set<int> s;
    encryptMatrix.clear();
    decryptMatrix.clear();
    encryptMatrix.resize(1 << 7);
    decryptMatrix.resize(1 << 7);

    int encryptMatrixPtr = 0;
    const int PRIME = 1000003;

    while (s.size() != (1 << 6)) {
        int randNum = abs(((int) (rand() * PRIME)) % (1 << 7));
        if (s.find(randNum) == s.end()) {
            encryptMatrix[encryptMatrixPtr] = randNum;
            decryptMatrix[randNum] = encryptMatrixPtr;
            s.insert(randNum);
            encryptMatrixPtr++;
        }
    }

    for (int i = 0; i < (1 << 7); i++) {
        if (s.find(i) == s.end()) {
            encryptMatrix[encryptMatrixPtr] = i;
            decryptMatrix[i] = encryptMatrixPtr;
            encryptMatrixPtr++;
        }
    }
}

extern "C" JNIEXPORT jintArray  JNICALL Java_com_sonudoo_AccountKeeper_Cipher_encrypt
        (JNIEnv *env, jobject obj, jint passcode, jintArray array) {
    jsize len = (env)->GetArrayLength(array);
    jint *body = (env)->GetIntArrayElements(array, 0);

    int arr[(int) len];

    std::vector<int> encryptMatrix;
    std::vector<int> decryptMatrix;

    generateMatrix((int) passcode, encryptMatrix, decryptMatrix);


    for (int i = 0; i < len; i++) {
        arr[i] = encryptMatrix[(int) body[i]];
    }
    jintArray result = (env)->NewIntArray(len);
    (env)->SetIntArrayRegion(result, 0, len, arr);

    return result;
}

JNIEXPORT jintArray  JNICALL Java_com_sonudoo_AccountKeeper_Cipher_decrypt
        (JNIEnv *env, jobject obj, jint passcode, jintArray array) {
    jsize len = (env)->GetArrayLength(array);
    jint *body = (env)->GetIntArrayElements(array, 0);

    int arr[(int) len];

    std::vector<int> encryptMatrix;
    std::vector<int> decryptMatrix;

    generateMatrix(passcode, encryptMatrix, decryptMatrix);

    for (int i = 0; i < len; i++) {
        arr[i] = decryptMatrix[(int) body[i]];
    }

    jintArray result = (env)->NewIntArray(len);
    (env)->SetIntArrayRegion(result, 0, len, arr);
    return result;
}
