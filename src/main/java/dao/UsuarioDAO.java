package dao;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import model.Usuario;

public class UsuarioDAO {
	
	public void cadastrar(Usuario usuario) throws Exception{
		
		String sql = "INSERT INTO usuario (nome, emil, senha) VALLUES (?,?,?)";
		
		try(Connection connection = ConexaoDB.getConexao(); 
			PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1,usuario.getNome());
			statement.setString(2,usuario.getEmail());
			statement.setString(3,usuario.getSenha());
			
		} catch (SQLException e) {
			throw new SQLException("Erro ao inserir os dados do usuario");
		}
	}
	
	public Usuario autenticar(String email, String senha) throws Exception {
		
		String sql = "SELECT id, nome, email, senha FROM usuario WHERE email = ?";
		
		try(Connection connection = ConexaoDB.getConexao();
			PreparedStatement statement = connection.prepareStatement(sql)) {
			
			statement.setString(1, email.trim().toLowerCase());
			
			try(ResultSet result = statement.executeQuery()) {
				if(result.next() && BCrypt.checkpw(senha, result.getString("senha"))) {
					return new Usuario(result.getInt("id"), result.getString("nome"), result.getString("email"));
				}
			} catch (Exception e) {
				throw new SQLException("Credencial inválida");
			}
			
		} catch (Exception e) {
			throw new SQLException("Credenciais inválidas");
		}
		
		return null;
	}

}
