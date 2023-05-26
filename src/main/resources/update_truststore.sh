#!/bin/bash

# Função para verificar se um alias já existe no truststore
alias_exists_in_truststore() {
  local alias=$1
  local truststore_path=$2
  local truststore_password=$3

  # Verificar se o alias existe no truststore
  keytool -list -alias "$alias" -keystore "$truststore_path" -storepass "$truststore_password" >/dev/null 2>&1

  return $?
}

# Função para adicionar ou atualizar um certificado no truststore
add_or_update_certificate_in_truststore() {
  local url=$1
  local truststore_path=$2
  local truststore_password=$3

  # Extrair o nome do host a partir da URL
  local host=$(echo "$url" | awk -F/ '{print $3}')
  # Nome do alias para o certificado
  local alias="$host"

  # Baixar o certificado do serviço da web
  openssl s_client -connect "$host:443" -showcerts </dev/null 2>/dev/null | sed -n '/BEGIN CERTIFICATE/,/END CERTIFICATE/p' >"$alias.crt"

  # Verificar se o alias já existe no truststore
  if alias_exists_in_truststore "$alias" "$truststore_path" "$truststore_password"; then
    # Atualizar o certificado existente
    keytool -delete -alias "$alias" -keystore "$truststore_path" -storepass "$truststore_password" -noprompt
  fi

  # Importar o certificado no truststore
  keytool -importcert -alias "$alias" -file "$alias.crt" -keystore "$truststore_path" -storepass "$truststore_password" -noprompt

  # Remover o arquivo do certificado
  rm "$alias.crt"

  echo "Certificado $alias adicionado/atualizado com sucesso no truststore."
}

# Array com a lista de URLs dos serviços da web
urls=(
  "https://nfe.svrs.rs.gov.br"
  "https://www1.nfe.fazenda.gov.br"
)

# Caminho para o arquivo truststore
truststore_path="truststore.jks"
# Senha do truststore
truststore_password="changeit"

# Loop para atualizar os certificados
for url in "${urls[@]}"; do
  add_or_update_certificate_in_truststore "$url" "$truststore_path" "$truststore_password"
done

echo "Truststore atualizado com sucesso."