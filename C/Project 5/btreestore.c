#include "btreestore.h"

void free_info(struct info *infos) {
    struct info *cur = infos;
    if (cur == NULL) {
        return;
    }
    while (cur->next != NULL) {
        struct info *tmp = cur->next;
        free(cur->data);
        free(cur);
        cur = tmp;
    }
    free(cur->data);
    free(cur);
    return;
}

void close_store(void *helper) {
    struct tree *tree = (struct tree *) helper;
    int i = 0;
    while (tree->freer[i] != NULL) {
        //printf("freed one\n");
        free(tree->freer[i]->keys);
        free(tree->freer[i]->children);
        free(tree->freer[i]);
        i++;
    }
    free(tree->freer);
    free_info(tree->infos);
    free(helper);
    return;
}

void add_node_to_freer(struct node *node, struct node **freer) {
    //printf("insert one\n");
    for (int i = 0; i < 200; ++i) {
        if (freer[i] == NULL) {
            freer[i] = node;
            return;
        }
    }
}

void remove_node_from_freer(struct node *node, struct node **freer) {
    //printf("remove one\n");
    for (int i = 0; i < 200; ++i) {
        if (freer[i] == node) {
            free(node->children);
            free(node->keys);
            free(node);
            while (freer[i] != NULL) {
                freer[i] = freer[i + 1];
                i++;
            }
            return;
        }
    }
}

void insert_child(struct node *node, int index, struct node *left, struct node *right) {
    int current_index = node->num_keys - 1;
    while (index < current_index) {
        node->children[current_index + 1] = node->children[current_index];
        current_index--;
    }
    node->children[current_index + 1] = right;
    node->children[current_index] = left;
}

struct node *new_node(struct node *parent, uint16_t branching, struct node **freer) {
    //printf("a new node;\n");
    struct node *a = (struct node *) malloc(sizeof(struct node));
    add_node_to_freer(a, freer);
    a->keys = (uint32_t *) malloc(branching * sizeof(uint32_t));
    a->children = (struct node **) malloc((branching + 1) * sizeof(struct node **));
    memset(a->children, 0, (branching + 1) * sizeof(struct node **));
    a->num_keys = 0;
    a->parent = parent;
    a->is_leaf = 1;
    return a;
}

void *init_store(uint16_t branching, uint8_t n_processors) {
    // Your code here
    struct tree *helper = (struct tree *) malloc(sizeof(struct tree));
    helper->freer = (struct node **) malloc(200 * sizeof(struct node *));
    memset(helper->freer, 0, 200);
    helper->branching = branching;
    //printf("branching: %d",branching);
    helper->infos = NULL;
    struct node *head = new_node(NULL, branching, helper->freer);
    helper->head = head;
    return helper;
}

struct info *find_info(uint32_t key, struct info *infos) {
    if (infos == NULL) {
        return NULL;
    } else {
        struct info *cur = infos;
        while (cur->next != NULL) {
            if (cur->ref == key) {
                return cur;
            } else {
                cur = cur->next;
            }
        }
        if (cur->ref == key) {
            return cur;
        } else {
            return NULL;
        }
    }
}

struct info *generate_info(uint32_t key, void *plaintext, size_t count, uint32_t encryption_key[4], uint64_t nonce) {
    struct info *info = (struct info *) malloc(sizeof(struct info));
    info->next = NULL;
    info->ref = key;
    memcpy(info->key, encryption_key, 4 * sizeof(int));
    info->size = count;
    info->nonce = nonce;
    info->data = malloc(((count + 7) / 8) * sizeof(uint64_t));
    uint64_t *plain = (uint64_t *) malloc(((count + 7) / 8) * sizeof(uint64_t));
    memcpy(plain, plaintext, count);
    //printf("malloc count using: %d\n",(count+7)/8);
    //printf("actually count using: %d\n",(count+7)/8);
    encrypt_tea_ctr(plain, encryption_key, nonce, info->data, (count + 7) / 8);
    free(plain);
    return info;
}

void insert_info(struct tree *tree, struct info *info) {
    struct info *cur = tree->infos;
    if (tree->infos == NULL) {
        tree->infos = info;
        return;
    } else {
        while (cur->next != NULL) {
            cur = cur->next;
        }
        cur->next = info;
        return;
    }
}

int insert_key(struct node *node, uint32_t key) {
    //printf("trying to insert %d into node\n",key);
    int i = node->num_keys;
    if (i == 0) {
        *node->keys = key;
        node->num_keys = 1;
        return 1;
    }
    while (key < *(uint32_t * )(node->keys + i - 1)) {
        *(uint32_t * )(node->keys + i) = *(uint32_t * )(node->keys + i - 1);
        //printf("value %d has been moved to next when i = %d\n",*(uint32_t*)(node->keys+i-1),i);
        i--;
        if (i == 0) {
            break;
        }
    }
    *(uint32_t * )(node->keys + i) = key;
    node->num_keys++;
    return i;
}

int find_key(struct node *node, uint32_t key) {
    if (node->num_keys == 0) {
        return 0;
    }
    int i = 0;
    uint32_t current_key = *(node->keys);
    while (i < node->num_keys) {
        if (current_key == key) {
            return -1;
        } else if (current_key > key) {
            return i;
        } else if (i + 1 == node->num_keys) {
            return node->num_keys;
        } else {
            i++;
            current_key = *(uint32_t * )(node->keys + i);
        }
    }
    //should never reach
    return -2;
}

int find_exist_key(struct node *node, uint32_t key) {
    if (node->num_keys == 0) {
        return 0;
    }
    for (int i = 0; i < node->num_keys; ++i) {
        if (node->keys[i] == key){
            return i;
        }
    }
    return -1;
}

void split(struct node *node, uint16_t branching, struct tree *tree) {
    int mid_index = branching / 2 - (branching + 1) % 2; //K median
    //printf("mid index is %d\n",mid_index);
    struct node *left_node = new_node(NULL, branching, tree->freer);
    struct node *right_node = new_node(NULL, branching, tree->freer);
    left_node->num_keys = mid_index;
    memcpy(left_node->keys, node->keys, mid_index * sizeof(uint32_t));
    right_node->num_keys = branching - mid_index - 1;
    memcpy(right_node->keys, node->keys + mid_index + 1, (right_node->num_keys) * sizeof(uint32_t));
    if (node->is_leaf == 0) {
        //printf("splitting not-leaf node\n");
        left_node->is_leaf = 0;
        right_node->is_leaf = 0;
        for (int i = 0; i <= mid_index; i++) {
            left_node->children[i] = node->children[i];
            if (left_node->children[i] != NULL) {
                //printf("content: %d\n",*right_node->children[i]->keys);
                left_node->children[i]->parent = left_node;
            }
        }
        //printf("The left's first child has %d keys\n",(*(left_node->children))->num_keys);
        for (int i = 0; i <= (branching / 2); i++) {
            //printf("i: %d\n",i);
            right_node->children[i] = *(node->children + mid_index + 1 + i);
            if (right_node->children[i] != NULL) {
                //printf("content: %d\n",*right_node->children[i]->keys);
                right_node->children[i]->parent = right_node;
            }
        }
        //printf("The right's first child has %d keys\n",(*(right_node->children))->num_keys);
        //printf("succeed\n");
    }
    if (node->parent == NULL) {
        //printf("doing head node\n");
        node->num_keys = 1;
        *(node->keys) = *(node->keys + mid_index);
        node->is_leaf = 0;
        *(node->children) = left_node;
        *(node->children + 1) = right_node;
        left_node->parent = node;
        right_node->parent = node;
        //printf("succeed\n");
    } else {
        //printf("mid num: %d\n",*(node->keys + mid_index));
        //printf("parent's first key: %d",*node->parent->keys);
        int index = insert_key(node->parent, *(node->keys + mid_index));
        insert_child(node->parent, index, left_node, right_node);
        //printf("index: %d\n",index);
        left_node->parent = node->parent;
        right_node->parent = node->parent;
        //printf("freed a node to make it left and right;\n");
        remove_node_from_freer(node, tree->freer);
    }
    if (right_node->parent->num_keys > branching - 1) {
        split(right_node->parent, branching, tree);
    }
}

int btree_insert(uint32_t key, void *plaintext, size_t count, uint32_t encryption_key[4], uint64_t nonce, void *helper) {
    // Your code here
    //printf("inserting %d\n ",key);
    struct tree *tree = (struct tree *) helper;
    int branching = tree->branching;
    struct node *node = tree->head;
    while (node != NULL) {
        int index = find_key(node, key);
        if (index == -1) {
            //printf("Inserting a key that is already existed, quit\n");
            exit(1);
        } else if (node->is_leaf) {
            insert_key(node, key);
            if (node->num_keys > branching - 1) {
                split(node, branching, tree);
            }
            struct info *info = generate_info(key, plaintext, count, encryption_key, nonce);
            insert_info(tree, info);
            return 0;
        } else {
            node = *(node->children + index);
        }
    }
    return -1;
}

int btree_retrieve(uint32_t key, struct info *found, void *helper) {
    // Your code here
    struct tree *tree = (struct tree *) helper;
    int branching = tree->branching;
    struct node *node = tree->head;
    while (node != NULL) {
        int index = find_key(node, key);
        if (index == -1) {
            //found
            struct info *info = find_info(key, tree->infos);
            if (info == NULL) {
                return -1;
            } else {
                found->size = info->size;
                found->data = info->data;
                found->nonce = info->nonce;
                memcpy(found->key, info->key, 4 * sizeof(uint32_t));
                return 0;
            }
        } else if (node->is_leaf) {
            return -1;
        } else {
            node = *(node->children + index);
        }
    }
    return -1;
}

int btree_decrypt(uint32_t key, void *output, void *helper) {
    struct info *found = (struct info *) malloc(sizeof(struct info));
    int stats = btree_retrieve(key, found, helper);
    if (stats == -1) {
        free(found);
        return -1;
    }
    uint64_t *plain = (uint64_t *) malloc(((found->size + 7) / 8) * sizeof(uint64_t));
    decrypt_tea_ctr(found->data, found->key, found->nonce, plain, (found->size + 7) / 8);
    memcpy(output, plain, found->size);
    free(plain);
    free(found);
    // Your code here
    return 0;
}

int remove_key(struct node *node, uint32_t key) {
    for (int i = 0; i < node->num_keys; ++i) {
        uint32_t cur = node->keys[i];
        if (cur == key) {
            while (i + 1 < node->num_keys) {
                node->keys[i] = node->keys[i + 1];
                i++;
            }
            node->num_keys--;
            return 1;
        } else if (cur > key) {
            return -1;
        }
    }
    return -1;
}

int find_node(struct node *node, struct node *target) {
    if (node->is_leaf) { return -1; }
    for (int i = 0; i <= node->num_keys; ++i) {
        if (node->children[i] == target) {
            return i;
        }
    }
    return -1;
}

int remove_node(struct node* node, struct node* target,struct node** freer){
    for (int i = 0; i <= node->num_keys; ++i) {
        if (node->children[i] == target){
            while (i+1<=node->num_keys) {
                node->children[i] = node->children[i+1];
                i++;
            }
            remove_node_from_freer(target,freer);
            return 0;
        }
    }
    return -1;
}
//merge two nodes into first one
int merge(struct node* left,uint32_t split_key,struct node* right, struct node** freer){
    uint32_t num_key = left->num_keys + right->num_keys +1;
    left->keys[left->num_keys] = split_key;
    int cnt = 0;
    for (int i = left->num_keys+1; i < num_key; ++i) {
        left->keys[i] = right->keys[cnt];
        left->children[i] = right->children[cnt];
        cnt++;
    }
    left->num_keys = num_key;
    remove_node(left->parent,right,freer);
    remove_key(left->parent,split_key);
}

int btree_delete(uint32_t key, void *helper) {
    // Your code here
    struct tree *tree = (struct tree *) helper;
    int branching = tree->branching;
    struct node *node = tree->head;
    while (node != NULL) {
        int index = find_key(node, key);
        //printf("index: %d\n",index);
        if (index == -1) {
            if (node->is_leaf) {
                if (node->num_keys - 1 >= branching / 2 || node->parent == NULL) {
                    int i = remove_key(node, key);
                    if (i == -1) {
                        printf("remove failed\n");
                        return -1;
                    }
                } else {
                    //printf("correct here\n");
                    int index = find_node(node->parent, node);
                    //printf("index of node: %d\n",index);
                    int found = 0;
                    if (index > 0) {
                        struct node *left = node->parent->children[index - 1];
                        if ((left->num_keys - 1) >= branching/2) {
                            //move left node
                            found = 1;
                            uint32_t left_biggest = left->keys[left->num_keys - 1];
                            uint32_t key_to_insert = node->parent->keys[index - 1];
                            node->parent->keys[index - 1] = left_biggest;
                            remove_key(node, key);
                            insert_key(node, key_to_insert);
                        }
                    }
                    if (found == 0 && index < node->parent->num_keys) {
                        struct node *right = node->parent->children[index+1];
                        //printf("right's num of keys: %d\n",right->num_keys);
                        if ((right->num_keys - 1) >= branching/2) {
                            //move right node
                            //printf("using right\n");
                            found = 1;
                            uint32_t right_smallest = right->keys[0];
                            uint32_t key_to_insert = node->parent->keys[index];
                            node->parent->keys[index] = right_smallest;
                            remove_key(node, key);
                            insert_key(node, key_to_insert);
                            remove_key(right,right_smallest);
                        }
                    }
                    if (found) {
                        return 0;
                    } else {
                        if (index > 0){
                            //merge with left
                            merge(node->parent->children[index-1],node->parent->keys[index-1],node,tree->freer);
                        }else{
                            //merge with right
                            merge(node,node->parent->keys[index],node->parent->children[index+1],tree->freer);
                        }
                        if (node->parent->num_keys>=branching/2){
                            return 0;
                        }else{
                            return -1;
                        }
                    }
                }
            } else {
                int index = find_exist_key(node,key);
                struct node* cur = node->children[index];
                while (cur->is_leaf == 0){
                    cur = cur->children[cur->num_keys];
                }
                uint32_t found_key = cur->keys[cur->num_keys-1];
                cur->keys[cur->num_keys-1] = key;
                node->keys[index] = found_key;
                node =cur;
            }
        } else if (node->is_leaf) {
            return -1;
        } else {
            node = *(node->children + index);
        }
    }
    return -1;
}

void node_export(struct node *node, struct node *list, int *size, int branching) {
    //printf("key: %d; leaf: %d\n",*node->keys,node->is_leaf);
    //if (*size >= 1){return;}
    //printf("exporting a node having %d keys where the first key is %d\n",node->num_keys,*(node->keys));
    if (node == NULL) {
        return;
    }
    if (node->is_leaf == 1 && node->num_keys > 0) {
        //printf("this key is a leaf\n");
        list[*size].num_keys = node->num_keys;
        //printf("num: %d\n",list[*size].num_keys);
        list[*size].keys = (uint32_t *) malloc(list[*size].num_keys * sizeof(uint32_t));
        for (int i = 0; i < node->num_keys; ++i) {
            list[*size].keys[i] = node->keys[i];
            //printf("the key in temp: %d, the key should be copied: %d, num:%d\n",my_list[*size].keys[i], node->keys[i], node->num_keys);
        }
        //printf("the key should be copied: %d num:%d\n",*(node->keys), node->num_keys);

        //printf("the key actually copied: %d num:%d\n",*(temp->keys), node->num_keys);
        //printf("index: %d",*size);
        (*size)++;
        return;
    } else if (node->is_leaf == 1) {
        //printf("an invalid node has been skipped\n");
        return;
    } else {
        if (node->num_keys == 0) {
            printf("????");
            exit(1);
        }
        //printf("this key is internal\n");
        list[*size].num_keys = node->num_keys;
        //printf("num: %d\n",list[*size].num_keys);
        list[*size].keys = (uint32_t *) malloc(list[*size].num_keys * sizeof(uint32_t));
        for (int i = 0; i < node->num_keys; ++i) {
            list[*size].keys[i] = node->keys[i];
            //printf("the key in temp: %d, the key should be copied: %d, num:%d\n",my_list[*size].keys[i], node->keys[i], node->num_keys);
        }
        //printf("index: %d",*size);
        //printf("%d;",*size);
        (*size)++;
        //printf("start exporting its children\n");
        for (int i = 0; i <= node->num_keys; ++i) {
            node_export(node->children[i], list, size, branching);
        }
        //printf("finish exporting this node\n");
        return;
    }
}

uint64_t btree_export(void *helper, struct node **list) {
    // Your code here
    //printf("start exporting\n");
    struct tree *tree = (struct tree *) helper;
    int branching = tree->branching;
    struct node *node = tree->head;
    int size = 0;
    struct node *my_list = (struct node *) malloc(100 * sizeof(struct node));
    *list = my_list;
    node_export(node, my_list, &size, branching);
    return size;
}

void encrypt_tea(uint32_t plain[2], uint32_t cipher[2], uint32_t key[4]) {
    // Your code here
    uint32_t sum = 0;
    uint32_t delta = 0x9E3779B9;
    cipher[0] = plain[0];
    cipher[1] = plain[1];
    //uint64_t x = pow(2,32);
    for (int i = 0; i < 1024; i++) {
        sum = (sum + delta) % (((uint64_t) 1 << 32));
        uint32_t tmp1 = ((cipher[1] << 4) + key[0]) % (((uint64_t) 1 << 32));
        uint32_t tmp2 = (cipher[1] + sum) % (((uint64_t) 1 << 32));
        uint32_t tmp3 = ((cipher[1] >> 5) + key[1]) % (((uint64_t) 1 << 32));
        cipher[0] = (cipher[0] + (tmp1 ^ tmp2 ^ tmp3)) % ((uint64_t) 1 << 32);
        uint32_t tmp4 = ((cipher[0] << 4) + key[2]) % (((uint64_t) 1 << 32));
        uint32_t tmp5 = (cipher[0] + sum) % (((uint64_t) 1 << 32));
        uint32_t tmp6 = ((cipher[0] >> 5) + key[3]) % (((uint64_t) 1 << 32));
        cipher[1] = (cipher[1] + (tmp4 ^ tmp5 ^ tmp6)) % ((uint64_t) 1 << 32);
    }
    return;
}

void decrypt_tea(uint32_t cipher[2], uint32_t plain[2], uint32_t key[4]) {
    // Your code here
    uint32_t sum = 0xDDE6E400;
    uint32_t delta = 0x9E3779B9;
    for (int i = 0; i < 1024; i++) {
        uint32_t tmp4 = ((cipher[0] << 4) + key[2]) % (((uint64_t) 1 << 32));
        uint32_t tmp5 = (cipher[0] + sum) % (((uint64_t) 1 << 32));
        uint32_t tmp6 = ((cipher[0] >> 5) + key[3]) % (((uint64_t) 1 << 32));
        cipher[1] = (cipher[1] - (tmp4 ^ tmp5 ^ tmp6)) % ((uint64_t) 1 << 32);
        uint32_t tmp1 = ((cipher[1] << 4) + key[0]) % (((uint64_t) 1 << 32));
        uint32_t tmp2 = (cipher[1] + sum) % (((uint64_t) 1 << 32));
        uint32_t tmp3 = ((cipher[1] >> 5) + key[1]) % (((uint64_t) 1 << 32));
        cipher[0] = (cipher[0] - (tmp1 ^ tmp2 ^ tmp3)) % ((uint64_t) 1 << 32);
        sum = (sum - delta) % (((uint64_t) 1 << 32));
    }
    plain[0] = cipher[0];
    plain[1] = cipher[1];
    return;
}

void encrypt_tea_ctr(uint64_t *plain, uint32_t key[4], uint64_t nonce, uint64_t *cipher, uint32_t num_blocks) {
    // Your code here
    for (int i = 0; i < num_blocks; i++) {
        uint64_t tmp1 = i ^nonce;
        uint64_t tmp2;
        encrypt_tea((uint32_t * ) & tmp1, (uint32_t * ) & tmp2, key);
        cipher[i] = plain[i] ^ tmp2;
    }
    return;
}

void decrypt_tea_ctr(uint64_t *cipher, uint32_t key[4], uint64_t nonce, uint64_t *plain, uint32_t num_blocks) {
    // Your code here
    //printf("num_blocks: %d\n",num_blocks);
    for (int i = 0; i < num_blocks; i++) {
        //printf("%d i\n",i);
        uint64_t tmp1 = i ^nonce;
        uint64_t tmp2;
        encrypt_tea((uint32_t * ) & tmp1, (uint32_t * ) & tmp2, key);
        if (i == num_blocks - 1) {
            uint64_t msg = cipher[i] ^tmp2;
            if ((msg % ((uint64_t) 1 << 32)) == 0) {
                *(((uint32_t *) plain) + 2 * i) = msg >> 32;
                break;
            }
        }
        plain[i] = cipher[i] ^ tmp2;
    }
    return;
}

