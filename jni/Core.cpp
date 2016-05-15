/*
 * Core.cpp
 *
 *  Created on: May 26, 2014
 *      Author: sgold
 */
#include <algorithm> //random_shuffle
#include <ctime>
#include <cstdlib>
#include <iostream>
#include <jni.h>
#include <android/log.h>
#include <string>
using namespace std;


//int myrandom (int i) { return rand()%i;}


int getns(int n) {
	if (n < 7)
		return 2;
	else if (n < 10)
		return 3;
	else
		return 4;
}
// Given a number of players n, generate an allegiance string with ns spies
void generateAllegiances(int n,int* allgs,bool* avalon) {
	//for (int i=0; i<n; i++) cout << ' ' << indices[i];
	// cout << '\n';
	// int allgs [n];
	// Ordinary Resistance: 0
	// Ordinary Spies: -1
	// Merlin: 2
	// Percival: 1
	// Morgana: -2
	int ns  = getns(n);
	int start = 0;
	int finish = n;
	if (avalon[0]) { //Merlin
		allgs[finish-1] = 2;
		finish -= 1;
	}
	if (avalon[1]) { //Percival and Morgana
		allgs[finish-1] = 1;
		allgs[start] = -2;
		start++;
		finish -= 1;
	}
	for (int i=start; i<finish; i++) allgs[i] = (i < ns) ? -1 : 0;
	random_shuffle(allgs,allgs+n);
}
void display() {
	srand(unsigned (time(0)));
	int n;
	printf ("Enter the number of players: ");
	scanf("%d",&n);
	int allgs [n];
	bool av [2] = {false,false};
	generateAllegiances(n,allgs,av);
	for (int i=0; i<n; i++) cout << ' ' << allgs[i];
	cout << '\n';
}

struct Mission{
	int numPlayers;
	int done;
	int numreqFails;
	bool evalResult(bool* responses);

	Mission()
	{
		numPlayers = 0;
		done=0; // +1 for mission pass, -1 for mission fail
		numreqFails=1;
	}
};

bool Mission::evalResult(bool* responses)
{
	int nFails = 0;
	for (int i = 0; i < this->numPlayers; i++) {
		nFails += (responses[i]) ? 0 : 1;
	}
	bool result = (nFails < this->numreqFails);
	string x = (result) ? "Pass" : "Fail";
	__android_log_print(ANDROID_LOG_INFO, "FROM_JNI", "[%s]", x.c_str());
	return result;
}



int nMissions = 5;

Mission* makeMissionRoster(int numPlayers) {
	int prtcpts [6][5] = {
			{2,3,2,3,3},
			{2,3,4,3,4},
			{2,3,3,4,4},
			{3,4,4,5,5},
			{3,4,4,5,5},
			{3,4,4,5,5}
	};
	Mission* roster = new Mission [nMissions];
	int *prtcptlist = prtcpts[numPlayers - 5];
	for (int i=0; i<nMissions; i++) roster[i].numPlayers = prtcptlist[i];
	if (numPlayers == 7) roster[3].numreqFails = 2;

	return roster;
}

Mission* gameRoster;

void setRoster(int numPlayers) {
	gameRoster = makeMissionRoster(numPlayers);
}

extern "C" {


JNIEXPORT jintArray JNICALL Java_com_aufbau_resistance_CollectionPagerAdapter_genAllgs(JNIEnv *env, jobject jobj, jint size, jbooleanArray avalon)
{
	jintArray result;
	int32_t csize = (int32_t) size;
	result = env->NewIntArray(csize);
	jboolean *body = env->GetBooleanArrayElements(avalon, NULL);
	jint capsLen = env->GetArrayLength(avalon);
	int clen = (int) capsLen;
	//__android_log_print(ANDROID_LOG_INFO, "FROM_JNI", "avalon size is %d", clen);

	if (result == NULL) {
		return NULL; /* out of memory error thrown */
	}
	int i;
	bool* cav = new bool[clen];
	for (int i = 0; i<clen; i++) cav[i] = (body[i] == JNI_TRUE);
	//__android_log_print(ANDROID_LOG_INFO, "FROM_JNI", "first av arg is %s", cav[0]?"true":"false");
	int fill[csize];
	generateAllegiances(csize,fill,cav);
	// move from the temp structure to the java structure
	env->SetIntArrayRegion(result,0,csize,fill);
	return result;
}
JNIEXPORT jboolean JNICALL Java_com_aufbau_resistance_ResultsCard_runMission(JNIEnv *env, jobject jobj, jobjectArray jmissions, jint index, jbooleanArray results) {
	Mission thisMission = gameRoster[index];
	int clen = thisMission.numPlayers;
	jboolean *res = env->GetBooleanArrayElements(results, NULL);
	bool* cav = new bool[clen];
	for (int i = 0; i<clen; i++) cav[i] = (res[i] == JNI_TRUE);
	// get the class
	jclass javaMissionClass = env->FindClass("com/aufbau/resistance/Mission");

	// get the field id
	jfieldID doneField = env->GetFieldID(javaMissionClass, "done", "I");
	jobject ithmiss = env->GetObjectArrayElement(jmissions, index);

	bool result = thisMission.evalResult(cav);
	int resval = (result) ? 1 : -1;
	env->SetIntField(ithmiss,doneField,resval);
	return (jboolean) result;
}
JNIEXPORT void JNICALL Java_com_aufbau_resistance_GameLoop_setMissions(JNIEnv *env, jobject jobj, jint numPlayers, jobjectArray jmissions) {
	setRoster((int)numPlayers);
	// Get a class reference for Mission
	jclass javaMissionClass = env->FindClass("com/aufbau/resistance/Mission");
	jfieldID npField = env->GetFieldID(javaMissionClass, "numPlayers", "I");
	if (npField == NULL) __android_log_print(ANDROID_LOG_WARN, "FROM_JNI", "Null Field ID!");



	// Get the value of each Integer object in the array
	jsize length = env->GetArrayLength(jmissions);
	int i;
	for (i = 0; i < length; i++) {
		jobject ithmiss = env->GetObjectArrayElement(jmissions, i);
		if (ithmiss == NULL) __android_log_print(ANDROID_LOG_WARN, "FROM_JNI", "Null Mission Element!");
		//jint value = env->CallIntMethod(env, objInteger, midIntValue);
		env->SetIntField(ithmiss,npField,gameRoster[i].numPlayers);
	}
}
}



