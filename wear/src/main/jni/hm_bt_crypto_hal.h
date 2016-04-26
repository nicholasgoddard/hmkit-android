
#include <stdint.h>

uint32_t hm_bt_crypto_hal_aes_ecb_block_encrypt(uint8_t *key, uint8_t *cleartext, uint8_t *cipertext);

uint32_t hm_bt_crypto_hal_ecc_get_ecdh(uint8_t *serial, uint8_t *ecdh);
uint32_t hm_bt_crypto_hal_ecc_add_signature(uint8_t *data, uint8_t size, uint8_t *signature);
uint32_t hm_bt_crypto_hal_ecc_validate_signature(uint8_t *data, uint8_t size, uint8_t *signature, uint8_t *serial);
uint32_t hm_bt_crypto_hal_ecc_validate_all_signatures(uint8_t *data, uint8_t size, uint8_t *signature);
uint32_t hm_bt_crypto_hal_ecc_validate_ca_signature(uint8_t *data, uint8_t size, uint8_t *signature);
uint32_t hm_bt_crypto_hal_hmac(uint8_t *key, uint8_t *data, uint8_t *hmac);
uint32_t hm_bt_crypto_hal_generate_nonce(uint8_t *nonce);