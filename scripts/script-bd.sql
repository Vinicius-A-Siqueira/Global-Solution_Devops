-- ============================================
-- Script de Criação do Banco de Dados
-- Projeto: WellMind API - Global Solution 2025
-- Database: Azure SQL Database
-- Conversão: Oracle → SQL Server
-- ============================================

-- Tabela: USUARIO
CREATE TABLE USUARIO (
    id_usuario BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    senha_hash NVARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    genero NVARCHAR(30),
    telefone NVARCHAR(20),
    data_cadastro DATETIME2 DEFAULT GETDATE() NOT NULL,
    status_ativo CHAR(1) DEFAULT 'S' NOT NULL,
    CONSTRAINT ck_usuario_status CHECK (status_ativo IN ('S', 'N')),
    CONSTRAINT ck_usuario_genero CHECK (genero IN ('Masculino', 'Feminino', 'Outro', 'Prefiro não informar'))
);

-- Tabela: EMPRESA
CREATE TABLE EMPRESA (
    id_empresa BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome_empresa NVARCHAR(200) NOT NULL,
    cnpj NVARCHAR(18) NOT NULL UNIQUE,
    endereco NVARCHAR(300),
    telefone NVARCHAR(20),
    email_contato NVARCHAR(100),
    data_cadastro DATETIME2 DEFAULT GETDATE() NOT NULL,
    CONSTRAINT ck_cnpj_formato CHECK (cnpj LIKE '[0-9][0-9].[0-9][0-9][0-9].[0-9][0-9][0-9]/[0-9][0-9][0-9][0-9]-[0-9][0-9]')
);

-- Tabela: USUARIO_EMPRESA (vínculo)
CREATE TABLE USUARIO_EMPRESA (
    id_usuario_empresa BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_empresa BIGINT NOT NULL,
    cargo NVARCHAR(100),
    data_vinculo DATETIME2 DEFAULT GETDATE() NOT NULL,
    status_vinculo CHAR(1) DEFAULT 'A' NOT NULL,
    CONSTRAINT fk_usuario_empresa_usuario FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_empresa_empresa FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa) ON DELETE CASCADE,
    CONSTRAINT ck_usuario_empresa_status CHECK (status_vinculo IN ('A', 'I')),
    CONSTRAINT uk_usuario_empresa UNIQUE (id_usuario, id_empresa)
);

-- Tabela: REGISTRO_BEMESTAR
CREATE TABLE REGISTRO_BEMESTAR (
    id_registro BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    data_registro DATETIME2 DEFAULT SYSDATETIME() NOT NULL,
    nivel_humor INT NOT NULL,
    nivel_estresse INT NOT NULL,
    nivel_energia INT NOT NULL,
    horas_sono DECIMAL(4,2),
    qualidade_sono INT,
    observacoes NVARCHAR(500),
    CONSTRAINT fk_registro_usuario FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    CONSTRAINT ck_nivel_humor CHECK (nivel_humor BETWEEN 1 AND 10),
    CONSTRAINT ck_nivel_estresse CHECK (nivel_estresse BETWEEN 1 AND 10),
    CONSTRAINT ck_nivel_energia CHECK (nivel_energia BETWEEN 1 AND 10),
    CONSTRAINT ck_horas_sono CHECK (horas_sono >= 0 AND horas_sono <= 24),
    CONSTRAINT ck_qualidade_sono CHECK (qualidade_sono BETWEEN 1 AND 10)
);

-- Tabela: CATEGORIA_RECOMENDACAO
CREATE TABLE CATEGORIA_RECOMENDACAO (
    id_categoria BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome_categoria NVARCHAR(100) NOT NULL UNIQUE,
    descricao NVARCHAR(300)
);

-- Tabela: PROFISSIONAL_SAUDE
CREATE TABLE PROFISSIONAL_SAUDE (
    id_profissional BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(100) NOT NULL,
    especialidade NVARCHAR(100) NOT NULL,
    crp_crm NVARCHAR(20) NOT NULL UNIQUE,
    email NVARCHAR(100),
    telefone NVARCHAR(20),
    disponivel CHAR(1) DEFAULT 'S' NOT NULL,
    CONSTRAINT ck_profissional_disponivel CHECK (disponivel IN ('S', 'N'))
);

-- ============================================
-- ÍNDICES PARA PERFORMANCE
-- ============================================

CREATE INDEX idx_usuario_email ON USUARIO(email);
CREATE INDEX idx_usuario_empresa_usuario ON USUARIO_EMPRESA(id_usuario);
CREATE INDEX idx_usuario_empresa_empresa ON USUARIO_EMPRESA(id_empresa);
CREATE INDEX idx_registro_usuario ON REGISTRO_BEMESTAR(id_usuario);
CREATE INDEX idx_registro_data ON REGISTRO_BEMESTAR(data_registro);
CREATE INDEX idx_empresa_cnpj ON EMPRESA(cnpj);

-- ============================================
-- DADOS DE EXEMPLO (SEED)
-- ============================================

-- Inserir Usuários
INSERT INTO USUARIO (nome, email, senha_hash, data_nascimento, genero, telefone, status_ativo) VALUES
('João Silva', 'joao.silva@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', '1990-05-15', 'Masculino', '11-98765-4321', 'S'),
('Maria Santos', 'maria.santos@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', '1985-08-22', 'Feminino', '11-97654-3210', 'S'),
('Carlos Oliveira', 'carlos.oliveira@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', '1992-12-10', 'Masculino', '11-96543-2109', 'S'),
('Ana Costa', 'ana.costa@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye', '1988-03-30', 'Feminino', '11-95432-1098', 'S');

-- Inserir Empresas
INSERT INTO EMPRESA (nome_empresa, cnpj, endereco, telefone, email_contato) VALUES
('TechCorp Brasil Ltda', '12.345.678/0001-90', 'Av. Paulista, 1000 - São Paulo/SP', '11-3000-0000', 'contato@techcorp.com.br'),
('Inovare Soluções SA', '98.765.432/0001-10', 'Rua Augusta, 500 - São Paulo/SP', '11-4000-0000', 'rh@inovare.com.br'),
('HealthTech Solutions', '11.222.333/0001-44', 'Av. Faria Lima, 2000 - São Paulo/SP', '11-5000-0000', 'contato@healthtech.com.br');

-- Vincular Usuários às Empresas
INSERT INTO USUARIO_EMPRESA (id_usuario, id_empresa, cargo, status_vinculo) VALUES
(1, 1, 'Desenvolvedor Full Stack', 'A'),
(2, 1, 'Analista de Sistemas', 'A'),
(3, 2, 'Gerente de Projetos', 'A'),
(4, 3, 'Psicóloga Organizacional', 'A');

-- Inserir Registros de Bem-Estar
INSERT INTO REGISTRO_BEMESTAR (id_usuario, nivel_humor, nivel_estresse, nivel_energia, horas_sono, qualidade_sono, observacoes) VALUES
(1, 7, 6, 7, 7.5, 8, 'Dia produtivo, mas com prazos apertados'),
(1, 8, 4, 8, 8.0, 9, 'Excelente dia de trabalho'),
(2, 6, 7, 5, 6.5, 6, 'Reuniões cansativas durante o dia'),
(2, 9, 3, 9, 8.5, 9, 'Ótimo final de semana, bem descansada'),
(3, 5, 8, 4, 5.0, 4, 'Estresse elevado devido a múltiplos projetos'),
(4, 8, 4, 8, 7.0, 8, 'Equilibrada e focada');

-- Inserir Categorias de Recomendação
INSERT INTO CATEGORIA_RECOMENDACAO (nome_categoria, descricao) VALUES
('Meditação e Mindfulness', 'Práticas de atenção plena e relaxamento'),
('Exercícios Físicos', 'Atividades para melhorar condicionamento físico'),
('Terapia e Apoio Psicológico', 'Suporte profissional para saúde mental'),
('Sono e Descanso', 'Técnicas para melhorar qualidade do sono'),
('Nutrição Equilibrada', 'Orientações sobre alimentação saudável'),
('Gestão de Tempo', 'Estratégias para organização e produtividade');

-- Inserir Profissionais de Saúde
INSERT INTO PROFISSIONAL_SAUDE (nome, especialidade, crp_crm, email, telefone, disponivel) VALUES
('Dr. Roberto Mendes', 'Psicologia Clínica', 'CRP 06/123456', 'roberto.mendes@psi.com.br', '11-91234-5678', 'S'),
('Dra. Fernanda Lima', 'Psiquiatria', 'CRM 12345', 'fernanda.lima@med.com.br', '11-92345-6789', 'S'),
('Profª. Carla Souza', 'Educação Física', 'CREF 123456', 'carla.souza@fit.com.br', '11-93456-7890', 'S'),
('Dr. Paulo Andrade', 'Nutrição', 'CRN 98765', 'paulo.andrade@nutri.com.br', '11-94567-8901', 'N');

-- ============================================
-- VIEWS ÚTEIS (OPCIONAL)
-- ============================================

-- View: Resumo de Bem-Estar por Usuário (últimos 30 dias)
CREATE VIEW vw_resumo_bemestar AS
SELECT 
    u.id_usuario,
    u.nome,
    u.email,
    COUNT(r.id_registro) AS total_registros,
    AVG(CAST(r.nivel_humor AS FLOAT)) AS media_humor,
    AVG(CAST(r.nivel_estresse AS FLOAT)) AS media_estresse,
    AVG(CAST(r.nivel_energia AS FLOAT)) AS media_energia,
    AVG(r.horas_sono) AS media_horas_sono,
    MAX(r.data_registro) AS ultimo_registro
FROM USUARIO u
LEFT JOIN REGISTRO_BEMESTAR r ON u.id_usuario = r.id_usuario
    AND r.data_registro >= DATEADD(DAY, -30, GETDATE())
GROUP BY u.id_usuario, u.nome, u.email;

-- View: Funcionários por Empresa
CREATE VIEW vw_funcionarios_empresa AS
SELECT 
    e.id_empresa,
    e.nome_empresa,
    e.cnpj,
    u.id_usuario,
    u.nome AS nome_funcionario,
    u.email,
    ue.cargo,
    ue.data_vinculo,
    ue.status_vinculo
FROM EMPRESA e
INNER JOIN USUARIO_EMPRESA ue ON e.id_empresa = ue.id_empresa
INNER JOIN USUARIO u ON ue.id_usuario = u.id_usuario;

GO
