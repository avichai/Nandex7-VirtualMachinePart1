#ifndef EX4OS_FBR_H
#define EX4OS_FBR_H

#include <iostream>
#include <list>
#include <unordered_map>
#include <unordered_set>


using namespace std;


/**
 * A file's block of data.
 */
class Block {
private:
    int fd;
    size_t  index;
    size_t refCounter;
    string data;

public:
    Block(int fd, size_t index, size_t refCounter, const string &data) :
            fd(fd), index(index), refCounter(refCounter), data(data) {}

    inline int getFd() const {
        return fd;
    }

    inline size_t getIndex() const {
        return index;
    }

    inline size_t getRefCounter() const {
        return refCounter;
    }

    inline const string &getData() const {
        return data;
    }

    inline void incRefCounter() {
        ++refCounter;
    }

};


/**
 * A class represents a cache which uses the FBR algorithm.
 */
class Cache {
private:
    list<Block*> *blockList;
    unordered_map<int, unordered_set<size_t>*>* blockMap;
    blksize_t blkSize;
    size_t nOldBlk;
    size_t nNewBlk;
    size_t cacheSize;

    bool isBlockInCache(int fd, size_t index);
    void removeBlockBFR();

public:
    Cache(size_t nOldBlk, int blkSize, size_t nNewBlk, size_t cacheSize);

    virtual ~Cache();

    int readData(char *buf, size_t size,
                   off_t offset, int fd);
};




#endif
