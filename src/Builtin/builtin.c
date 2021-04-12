#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

void print(const char *s) {printf("%s", s);}

void println(const char *s) {printf("%s\n", s);}

void printInt(int n) {printf("%d", n);}

void printlnInt(int n) {printf("%d\n", n);}

void *_malloc(int n) {return malloc(n);}

const char *getString()
{
	char *buf = malloc(sizeof(char) * 1024);
	scanf("%s", buf);
	return buf;
}

int getInt()
{
	int ret;
	scanf("%d", &ret);
	return ret;
}

const char *toString(int i)
{
	char *buf = malloc(sizeof(char) * 15);
	sprintf(buf, "%d", i);
	return buf;
}

const char *stringAdd(const char *lhs, const char *rhs)
{
	char *buf = malloc(sizeof(char) * 1024);
	strcpy(buf, lhs);
	strcat(buf, rhs);
	return buf;
}

bool stringIsEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) == 0;}

bool stringIsNotEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) != 0;}

bool stringIsLessThan(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) < 0;}

bool stringIsGreaterThan(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) > 0;}

bool stringIsLessThanOrEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) <= 0;}

bool stringIsGreaterThanOrEqual(const char *lhs, const char *rhs) {return strcmp(lhs, rhs) >= 0;}

int stringLength(const char *s) {return strlen(s);}

const char *stringSubstring(const char *s, int l, int r)
{
	char *buf = malloc(sizeof(char) * (r - l + 1));
	memcpy(buf, s + l, r - l);
	buf[r - l] = '\0';
	return buf;
}

int stringParseInt(const char *s)
{
	int ret;
	sscanf(s, "%d", &ret);
	return ret;
}

int stringOrd(const char *s, int pos) {return s[pos];}