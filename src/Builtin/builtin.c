#include <stdbool.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <stdio.h>

void print(char *s) {printf("%s", s);}

void println(char *s) {printf("%s\n", s);}

void printInt(int n) {printf("%d", n);}

void printlnInt(int n) {printf("%d\n", n);}

char *getString()
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

char *toString(int i)
{
	char *buf = malloc(sizeof(char) * 1024);
	int len = 0;
	for (;i;i /= 10) buf[len ++] = '0' + (i % 10);
	if (!len) buf[len ++] = '0';
	for (int l = 0;(l << 1) < len - 1;++ l)
	{
		char w = buf[l];
		buf[l] = buf[len - l - 1];
		buf[len - l - 1] = w;
	}
	buf[len] = '\0';
	return buf;
}

char *stringAdd(char *lhs, char *rhs)
{
	char *buf = malloc(sizeof(char) * 1024);
	char *ptr = buf;
	for (;*lhs != '\0';++ lhs, ++ ptr) *ptr = *lhs;
	for (;*rhs != '\0';++ rhs, ++ ptr) *ptr = *rhs;
	*ptr = '\0';
	return buf;
}

bool stringIsEqual(char *lhs, char *rhs)
{
	for (;*lhs != '\0' || *rhs != '\0';++ lhs, ++ rhs)
		if (*lhs != *rhs) return false;
	return true;
}

bool stringIsNotEqual(char *lhs, char *rhs)
{
	for (;*lhs != '\0' || *rhs != '\0';++ lhs, ++ rhs)
		if (*lhs != *rhs) return true;
	return false;
}

bool stringIsLessThan(char *lhs, char *rhs)
{
	for (;*lhs != '\0' || *rhs != '\0';++ lhs, ++ rhs)
	{
		if (*lhs < *rhs) return true;
		if (*lhs > *rhs) return false;
	}
	return false;
}

bool stringIsGreaterThan(char *lhs, char *rhs)
{
	for (;*lhs != '\0' || *rhs != '\0';++ lhs, ++ rhs)
	{
		if (*lhs > *rhs) return true;
		if (*lhs < *rhs) return false;
	}
	return false;
}

bool stringIsLessThanOrEqual(char *lhs, char *rhs)
{
	for (;*lhs != '\0' || *rhs != '\0';++ lhs, ++ rhs)
	{
		if (*lhs < *rhs) return true;
		if (*lhs > *rhs) return false;
	}
	return true;
}

bool stringIsGreaterThanOrEqual(char *lhs, char *rhs)
{
	for (;*lhs != '\0' || *rhs != '\0';++ lhs, ++ rhs)
	{
		if (*lhs > *rhs) return true;
		if (*lhs < *rhs) return false;
	}
	return true;
}

int stringLength(char *s)
{
	int len = 0;
	for (;*s != '\0';++ s, ++ len);
	return len;
}

char *stringSubstring(char *s, int l, int r)
{
	char *buf = malloc(sizeof(char) * 1024);
	for (;l;++ s, -- r, -- l);
	for (;r;++ s, -- r, ++ l) buf[l] = *s;
	buf[l] = '\0';
	return buf;
}

int stringParseInt(char *s)
{
	int ret = 0;
	for (;isdigit(*s);++ s) ret = ret * 10 + (*s - '0');
	return ret;
}

int stringOrd(char *s, int pos)
{
	for (;pos; ++ s, -- pos);
	return *s;
}