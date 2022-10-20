char *strcpy(char *dst, const char *src){
    char *tmp = dst;
    while ((*dst++ = *src++) != '\0');
    return tmp;
}

char* strcat ( char * dst , const char * src ) {
    char *cp = dst;
    while (*cp)
        cp++; //find end of dst
    while ((*cp++ = *src++));//Copy src to end of dst
    return (dst); //return dst
}

int strcmp (const char * src, const char * dst)
{
    int ret;
    //keep comparing the character pointed
    while( ! (ret = *(unsigned char *)src - *(unsigned char *)dst) && *dst)
        //move to next
        ++src, ++dst;
    if ( ret < 0 )
        ret = -1 ;
    else if ( ret > 0 )
        ret = 1 ;
    return( ret );
}